package com.newrelic;

import com.newrelic.jfr.summarizers.EventSummarizer;
import com.newrelic.jfr.summarizers.ObjectAllocationInNewTLABSummarizer;
import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordingFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.function.Predicate;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public final class JFRFileParser {

    // Configuring the logger like this just silently fails - no output is produced ...
    private static final Logger logger = Logger.getLogger(JFRFileParser.class.getName());

    // This does not work either - dies with an exception
//    static {
//        try {
//            LogManager.getLogManager().readConfiguration(JFRFileParser.class.getResourceAsStream("/logging.properties"));
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.exit(1);
//        }
//        logger = Logger.getLogger(JFRFileParser.class.getName());
//    }

    private static final int SUMMARY_PERIOD = 60;

    private final String filename;

    public JFRFileParser(String filename) {
        this.filename = filename;
    }

    private static Predicate<RecordedEvent> testMaker(String s) {
        return e -> e.getEventType().getName().startsWith(s);
    }

    private static final Map<Predicate<RecordedEvent>, EventSummarizer> summarizers =
            Map.of(testMaker("jdk.ObjectAllocationInNewTLAB"), new ObjectAllocationInNewTLABSummarizer());

    public void run() {
        try (var recordingFile = new RecordingFile(Paths.get(filename))) {
            var count = 0L;

            var metricBuffer = new MetricBuffer(new Attributes());
            LocalDateTime lastSent = null;

            while (recordingFile.hasMoreEvents()) {
                var event = recordingFile.readEvent();
                if (event != null) {
                    if (lastSent == null) {
                        lastSent = LocalDateTime.ofInstant(event.getStartTime(), ZoneOffset.UTC);
                    }

                    // Summarizers go here...
                    for (var ent : summarizers.entrySet()) {
                        if (ent.getKey().test(event)) {
                            var summarizer = ent.getValue();
                            summarizer.apply(event);

                            if (LocalDateTime.now().isAfter(lastSent.plusSeconds(SUMMARY_PERIOD))) {
                                summarizer.summarizeAndReset().forEach(metricBuffer::addMetric);
                                lastSent = LocalDateTime.ofInstant(event.getStartTime(), ZoneOffset.UTC);
                                count = count + 1;
                                logger.info("Summarizing a period...");
                            }
                        }
                    }
                    // Silently discard unhandled events
                }
            }
        } catch (IOException iox) {
            throw new RuntimeException(iox);
        }
    }

    public static void main(String[] args) {
        var processor = new JFRFileParser(args[0]);
        processor.run();
    }

}

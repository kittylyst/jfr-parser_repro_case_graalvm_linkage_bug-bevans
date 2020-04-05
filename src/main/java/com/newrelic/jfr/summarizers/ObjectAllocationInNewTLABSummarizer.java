package com.newrelic.jfr.summarizers;

import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordedThread;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * This class handles all TLAB allocation JFR events, and delegates them to the actual
 * aggregators, which operate on a per-thread basis
 */
public final class ObjectAllocationInNewTLABSummarizer implements EventSummarizer {

    public static final String EVENT_NAME = "jdk.ObjectAllocationInNewTLAB";

    private final Map<String, EventSummarizer> perThread = new HashMap<>();

    @Override
    public String getEventName() {
        return EVENT_NAME;
    }

    @Override
    public void apply(RecordedEvent ev) {
        var threadName = getThreadName(ev);
        threadName.ifPresent(thread -> {
            if (perThread.get(thread) == null) {
                perThread.put(thread,
                        new PerThreadObjectAllocationInNewTLABSummarizer(thread, ev.getStartTime().toEpochMilli()));
            }
            perThread.get(thread).apply(ev);
        });
    }

    /**
     * There are cases where the event has the wrong type inside it
     * for the thread, so calling {@link RecordedEvent#getThread(String)} internally
     * throws a {@link ClassCastException}. We work around it here by just
     * getting the raw value and checking the type.
     *
     * @param ev The event from which to carefully extract the thread
     * @return the thread name, or empty if unable to extract it
     */
    public static Optional<String> getThreadName(RecordedEvent ev) {
        Object thisField = ev.getValue("eventThread");
        if (thisField instanceof RecordedThread) {
            return Optional.of(((RecordedThread) thisField).getJavaName());
        }
        return Optional.empty();
    }

    @Override
    public Stream<Summary> summarizeAndReset() {
        return perThread
                .values()
                .stream()
                .flatMap(EventSummarizer::summarizeAndReset);
    }
}

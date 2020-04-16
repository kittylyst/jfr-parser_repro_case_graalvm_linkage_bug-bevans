# jfr-graal-repro 

Download and extract graalvm-ce-java11-20.0.0 from https://github.com/graalvm/graalvm-ce-builds/releases
then install the native-image component with:
```
$HOME/path/to/graalvm-ce-java11-20.0.0/bin/gu install native-image
```

To build the jar and the native image:

```
JAVA_HOME=$HOME/path/to/graalvm-ce-java11-20.0.0 mvn clean package
```

then use
```
./target/JFRFileParser <JFR File>
```
to run JFRFileParser as GraalVM native-image.

or

```
java -jar target/jfr-graal-repro-0.1.0-jar-with-dependencies.jar <JFR File>
```

to run JFRFileParser as a jar on JVM.

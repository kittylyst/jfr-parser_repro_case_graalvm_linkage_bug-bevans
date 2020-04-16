# jfr-graal-repro 

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

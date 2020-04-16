# jfr-graal-repro 

To build:

```
mvn clean compile assembly:single
```

and to run:

```
java -jar target/jfr-graal-repro-0.1.0-jar-with-dependencies.jar <JFR File>
```

and to compile native:

```
native-image -jar target/jfr-graal-repro-0.1.0-jar-with-dependencies.jar jfr-parse
```

then use
```
./jfr-parse <JFR File>
```

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
gcc -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin/" -c -o JVM.o JVM.c 
libtool -static -o libjfrstub.a JVM.o 
native-image -H:+JNI -H:JNIConfigurationFiles=./jni-config.json -H:CLibraryPath=. -jar target/jfr-graal-repro-0.1.0-jar-with-dependencies.jar
```

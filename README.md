# HOW TO TEST IT

Compile and build the executable jar

```
./mvnw clean package 
```

FTB I am changing GraalVM's node startup options in ./build/nodejvm file

and then to execute the test

```
./build/nodejvm -Dfile.encoding=UTF-8  -jar target/demo-0.0.1-SNAPSHOT.jar
```


This process will invoke 1000 times through NodeJVM's NodeJS class runtime.js (located in src/test/resources) 
with the 2 arguments : middleware.cjs and the html to process (located in src/test/resources too)

Although executions finish memory allocated for DynamiccObjecctBasic class is not GC, not even with explicit GC
 
NOTE: change log level in application.properties to check the output of the js execution
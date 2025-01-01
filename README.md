[![License](https://img.shields.io/github/license/toolarium/toolarium-processing-engine)](https://github.com/toolarium/toolarium-processing-engine/blob/master/LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.toolarium/toolarium-processing-engine/0.5.2)](https://search.maven.org/artifact/com.github.toolarium/toolarium-processing-engine/0.5.2/jar)
[![javadoc](https://javadoc.io/badge2/com.github.toolarium/toolarium-processing-engine/javadoc.svg)](https://javadoc.io/doc/com.github.toolarium/toolarium-processing-engine)

# toolarium-processing-engine

Implements an open source processing engine which allows to run so-called [processing units (ProcessingUnit)](https://github.com/toolarium/toolarium-processing-unit/) to be executed.

A ProcessingUnit is a simple java class that implements the main part of a processing. The framework is designed so that only the main part, the real processing, has to be implemented. 
This means that you do not have to write any loops.

The toolarium processing engine has the following features:
- Lightweight processing engine which is optimized for fast startups.
- Multi-threaded processing
- Supports fast and reliable suspending of the engine even its resuming.

## Built With

* [cb](https://github.com/toolarium/common-build) - The toolarium common build

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/toolarium/toolarium-processing-engine/tags). 


### Gradle:

```groovy
dependencies {
    implementation "com.github.toolarium:toolarium-processing-engine:0.5.2"
}
```

### Maven:

```xml
<dependency>
    <groupId>com.github.toolarium</groupId>
    <artifactId>toolarium-processing-engine</artifactId>
    <version>0.5.2</version>
</dependency>
```

## How to start
```java
    // Optional you can set a IProcessingUnitInstanceManager to control creating instances
    //ProcessingEngineFactory.getInstance().setProcessingUnitInstanceManager(instanceManager);

    // start process engine
    IProcessEngine processEngine = ProcessingEngineFactory.getInstance().getProcessingEngine();
    processEngine.addListener(new LogProcessingListener());

    // register processings
    IProcessingUnitDefinition p1 = processEngine.getProcessingUnitRegistry().register(ProcessingUnitSample.class);
    IProcessingUnitDefinition p2 = processEngine.getProcessingUnitRegistry().register(ProcessingUnitSample2.class.getName());

    // start processing
    IProcessingUnitRunnable runnable1 = processEngine.execute(UUID.randomUUID().toString(), "test1", p1.getProcessingClassname(),
                                                              List.of(new Parameter(ProcessingUnitSample.INPUT_FILENAME_PARAMETER.getKey(), "my-filename1")));
    assertNotNull(runnable1);

    IProcessingUnitRunnable runnable2 = processEngine.execute(UUID.randomUUID().toString(), "test2", p2.getProcessingClassname(),
                                                              List.of(new Parameter(ProcessingUnitSample2.INPUT_FILENAME_PARAMETER.getKey(), "my-filename2")));
    assertNotNull(runnable2);


    // wait processing
    while (processEngine.getStatus().getNumberOfRunningProcessings() > 0) {
       ThreadUtil.getInstance().sleep(500L);
    }
        
    // shutdown processing
    byte[] persistedContent = processEngine.shutdown();
    assertNull(persistedContent); // if there were no processings it is null!
```

## How to test
The class com.github.toolarium.processing.unit.runtime.test.TestProcessingUnit implements a simple test processing which can simulate all kind of behaviours.


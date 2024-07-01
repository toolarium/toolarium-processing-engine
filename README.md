[![License](https://img.shields.io/github/license/toolarium/toolarium-processing-engine)](https://github.com/toolarium/toolarium-processing-engine/blob/master/LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.toolarium/toolarium-processing-engine/0.4.0)](https://search.maven.org/artifact/com.github.toolarium/toolarium-processing-engine/0.4.0/jar)
[![javadoc](https://javadoc.io/badge2/com.github.toolarium/toolarium-processing-engine/javadoc.svg)](https://javadoc.io/doc/com.github.toolarium/toolarium-processing-engine)

# toolarium-processing-engine

Implements an open source processing engine which allows to run so-called [processing units (ProcessingUnit)](https://github.com/toolarium/toolarium-processing-unit/) to be executed.

A ProcessingUnit is a simple java class that implements the main part of a processing. The framework is designed so that only the main part, the real processing, has to be implemented. 
This means that you do not have to write any loops.

The toolarium processing engine has the following features:
- Lightweight processimg engine which is optimized for fast startups.

## Built With

* [cb](https://github.com/toolarium/common-build) - The toolarium common build

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/toolarium/toolarium-processing-engine/tags). 


### Gradle:

```groovy
dependencies {
    implementation "com.github.toolarium:toolarium-processing-engine:0.4.0"
}
```

### Maven:

```xml
<dependency>
    <groupId>com.github.toolarium</groupId>
    <artifactId>toolarium-processing-engine</artifactId>
    <version>0.4.0</version>
</dependency>
```




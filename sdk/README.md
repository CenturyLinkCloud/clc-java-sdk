Installation
------------

If you use Maven or Gradle, you can simply add the CLC SDK as a dependency.

Maven: 
```xml
<dependencies>
    <dependency>
        <groupId>com.centurylink.cloud</groupId>
        <artifactId>clc-java-sdk</artifactId>
        <version>1.2.3</version>
    </dependency>
</dependencies>
```

Gradle:
```groovy
compile 'com.centurylink.cloud:clc-java-sdk:1.2.3'
```


Build process
-------------

To build sources, you need to have Gradle 2.2.1. (or later) installed.

To check out and build the SDK, issue the following commands:

```
$ git clone git@github.com:CenturyLinkCloud/clc-java-sdk.git
$ cd clc-java-sdk/clc-java-sdk/sdk
$ gradle build
```

If you also want to exclude the test task, add `-x test`:
```
$ gradle build -x test
```

Tests configuration
-------------------
To run tests, you can either add the following JVM options:`-Dclc.username=username` and `-Dclc.password=password` 
or add these options to the `gradle` command.

To run tests, issue this command:
```
$ gradle clean test -Dclc.username=username -Dclc.password=password
```

To run integration tests, use:
```
$ gradle clean runIntegrationTests -Dclc.username=username -Dclc.password=password
```
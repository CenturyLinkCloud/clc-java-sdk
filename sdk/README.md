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
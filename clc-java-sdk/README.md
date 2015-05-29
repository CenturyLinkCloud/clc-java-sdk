Documentation
-------------
See the [wiki](.) for CLC Java SDK getting-started and user guides.

Build process details
---------------------
To build sources, you need to install Gradle 2.2.1 or later. To check out and build the ClcSDK source, issue the following commands:

```
$ git clone git@github.com:CenturyLinkCloud/clc-java-sdk.git
$ cd clc-java-sdk/clc-java-sdk
$ gradle build
```

Configuration details
---------------------

Please see the [SDK configuration](./2.11-SDK-configuration) section for details and examples of how to configure the CLC SDK.

Example
-------
This example shows some of the functionality supported by the CLC Java SDK:
```java
import static com.centurylink.cloud.sdk.servers.services.domain.InfrastructureConfig.dataCenter;
...
ClcSdk sdk = new ClcSdk(
    new DefaultCredentialsProvider("user", "password")
);

ServerService serverService = sdk.serverService();
GroupService groupService = sdk.groupService();

List<Group> groups = 
groupService
    .defineInfrastructure(dataCenter(DataCenter.DE_FRANKFURT).subitems(
        group("Root Group").subitems(
            group("Business").subitems(
                new CreateServerConfig()
                .name("SRV")
                .machine(new Machine()
                    .cpuCount(1)
                    .ram(2)
                )
                .template(Template.refByOs()
                    .dataCenter(US_CENTRAL_SALT_LAKE_CITY)
                    .type(CENTOS)
                    .version("6")
                    .architecture(x86_64)
                )
            )
        )
    )
).waitUntilComplete().getResult();
```

License
-------
This project is licensed under the [Apache License v2.0](http://www.apache.org/licenses/LICENSE-2.0.html).

Documentation
-------------
See the [wiki](https://github.com/CenturyLinkCloud/clc-java-sdk/wiki) for CLC Java SDK getting-started and user guides.

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

Please see the [SDK configuration](https://github.com/CenturyLinkCloud/clc-java-sdk/wiki/2.11-SDK-configuration)
section for details and examples of how to configure the CLC SDK.

Example
-------
This example shows some of the functionality supported by the CLC Java SDK:

```java
import static com.centurylink.cloud.sdk.servers.services.domain.InfrastructureConfig.dataCenter;

...
ClcSdk sdk = new ClcSdk("user", "password");

ServerService serverService = sdk.serverService();
GroupService groupService = sdk.groupService();

serverService
    .create(new CreateServerConfig()
        .name("TCRT")
        .type(STANDARD)
        .storageType(PREMIUM)
        .password("serverPass")
        .group(Group.refByName()
            .name(DEFAULT_GROUP)
            .dataCenter(DataCenter.refByName("FranKfUrt"))
        )
        .timeToLive(ZonedDateTime.now().plusDays(1))
        .machine(new Machine()
            .cpuCount(1)
            .ram(3)
            .disk(new DiskConfig()
                .type(DiskType.RAW)
                .size(14)
            )
        )
        .template(Template.refByOs()
            .dataCenter(DE_FRANKFURT)
            .type(CENTOS)
            .version("6")
            .architecture(x86_64)
        )
        .network(new NetworkConfig()
            .primaryDns("172.17.1.26")
            .secondaryDns("172.17.1.27")
            .publicIpConfig(new CreatePublicIpConfig()
                .openPorts(8080)
            )
        )
    )
    .waitUntilComplete();
```

License
-------
This project is licensed under the [Apache License v2.0](http://www.apache.org/licenses/LICENSE-2.0.html).

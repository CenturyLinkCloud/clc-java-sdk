
During this time implemented next functionality


Create Server Functionality
---------------------------

``` java

new ClcSdk()
    .serverService()
    .create(new CreateServerCommand()
        .name("TCRT")
        .type(STANDARD)
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
            .publicIp(new PublicIpConfig()
                .openPorts(8080)
            )
        )
    )
    .waitUntilComplete()

```

Delete Server
-----------------------

Delete single server

``` java

serverService.delete(newServer);

```

Delete set of servers

``` java

serverService
    .delete(
        Server.refById("DE1ALTDTCRT154"),
        Server.refById("DE1ALTDTCRT155"),
    );
    
```

Delete set of servers specified by search criteria

``` java

serverService
    .delete(new DataCenterFilter
    );
    
```

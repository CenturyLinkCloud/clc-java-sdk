

Modify Servers Functionality
----------------------------------

Implemented:

1. Modify CPU / RAM functionality
2. Add / Modify / Remove disks functionality


``` java

serverService.modify(serverRef,
    new ModifyServerConfig()
        .machineConfig(new Machine()
           .cpuCount(modifiedCpu)
               .ram(modifiedRam)
               .disk(
                   new DiskConfig().diskId("0:0").size(1)
               )
               .disk(
                   new DiskConfig().diskId("0:1").size(2)
               )
               .disk(
                   new DiskConfig().diskId("0:2").size(3)
               )
        )
        .description("New machine description")
    );

```


Super Command Functionality
-----------------------------

``` java

OperationFuture<List<Group>> results =
    groupService.defineInfrastructure(new InfrastructureConfig()
        .datacenter(DataCenter.DE_FRANKFURT).subitems(
            group("Root Group", "Root Group Description").subitems(
                group("Sub Group").subitems(
                    apacheHttpServer(),
                    mysqlServer()
                ),
                nginxServer(),
                new CreateServerConfig()
                    .name("SRV")
                    .type(STANDARD)

                    .machine(new Machine()
                        .cpuCount(1)
                        .ram(2)
                    )
            )
        )
    )

```

Group Billing Statistics Functionality
-----------------------------

Implemented:

1. Possibilities to receive billing statistics by groups and group filter


``` java

List<GroupBillingStats> results =
    serverService.getBillingStats(
        Group.refByName(DataCenter.DE_FRANKFURT, "Default Group")
    );


List<GroupBillingStats> results =
    serverService.getBillingStats(
        new GroupFilter()
            .dataCenters(dataCenter1, dataCenter2)
            .dataCentersWhere(d -> d.getGroup().equals("groupId"))
            .id("groupId1", "groupId2")
            .nameContains("MyGroup")
    );

```
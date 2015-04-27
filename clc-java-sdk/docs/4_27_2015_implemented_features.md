
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


Delete Server Functionality
-----------------------

Delete single server:

``` java

serverService.delete(newServer);

```

Delete set of servers:

``` java

serverService
    .delete(
        Server.refById("DE1ALTDTCRT154"),
        Server.refById("DE1ALTDTCRT155"),
    );
    
```

Delete set of servers specified by search criteria:

``` java

serverService
    .delete(new ServerFilter()
        .dataCenters(DataCenters.US_WEST_SEATTLE)
        .onlyActive()
    );
    
```


Search DataCenters Functionality
----------------------------------

``` java

List<DataCenterMetadata> results = 
    dataCenterService
        .find(new DataCenterFilter()
            .dataCenters(dataCenterRef)
            .id("va1", "ca1")
            .nameContains("FrankFurt")
            .where(d -> d.getGroup().equals("groupId"))
        );

```


Search Groups Functionality
-----------------------------

``` java

List<GroupMetadata> results = 
    groupServerService.find(
        new GroupFilter()
            .dataCenters(dataCenter1, dataCenter2)
            .dataCentersWhere(d -> d.getGroup().equals("groupId"))
            .id("groupId1", "groupId2")
            .nameContains("MyGroup")
    )

```


Search Templates Functionality
-----------------------------

``` java

TemplateMetadata metadata = 
    templateService
        .findByRef(Template.refByOs()
            .dataCenter(US_EAST_STERLING)
            .type(RHEL)
            .version("6")
            .architecture(x86_64)
        );

TemplateMetadata metadata = 
    templateService
        .findByRef(Template.refByName()
            .dataCenter(US_EAST_STERLING)
            .name("CENTOS-6-64-TEMPLATE")
        );

TemplateMetadata metadata = 
    templateService
        .findByRef(Template.refByDescription()
            .dataCenter(US_EAST_STERLING)
            .description("pxe boot")
        );

List<TemplateMetadata> results = 
    templateService
        .find(new TemplateFilter()
            .dataCenters(US_EAST_STERLING)
            .osTypes(new OsFilter()
                .type(CENTOS)
            )
        );
        
List<TemplateMetadata> results = 
    templateService
        .find(new TemplateFilter()
            .dataCenters(US_EAST_STERLING)
            .where(t -> t.getCapabilities().contains(MANAGED_OS_VALUE))
        );

```


Search Servers Functionality
-----------------------------

``` java

serverService
    .find(new ServerFilter()
        .dataCenters(dataCenter1, dataCenter2)
        .dataCentersWhere(d -> d.getGroup().equals("groupId"))
        .groupId("group1", "group2")
        .groups(Group.refByName()
            .dataCenter(DataCenters.US_EAST_NEW_YORK)
            .name("MyServers")
        )
        .groupsWhere(g -> g.getType().equals("default"))
        .status("active", "archived")
        .id("DE1ALTDTCRT154", "DE1ALTDTCRT155")
        .where(s -> s.getDetails().getInMaintenanceMode())
    );
    
```


Add public IP functionality
---------------------------

``` java

serverService
    .addPublicIp(serverRef,
        new PublicIpConfig()
            .openPorts(PortConfig.HTTPS, PortConfig.HTTP)
            .sourceRestrictions("70.100.60.140/32")
    );
    
serverService
    .addPublicIp(
        asList(
            serverRef1, serverRef2
        ),
        new PublicIpConfig()
            .openPorts(PortConfig.HTTPS, PortConfig.HTTP)
            .sourceRestrictions("70.100.60.140/32")
    );
    
serverService
    .addPublicIp(
        new ServerFilter()
            .dataCenters(DataCenters.US_WEST_SEATTLE)
            .onlyActive(),
        new PublicIpConfig()
            .openPorts(PortConfig.HTTPS, PortConfig.HTTP)
            .sourceRestrictions("70.100.60.140/32")
    );

```


Search public IP functionality
------------------------------

``` java

List<PublicIpMetadata> publicIps = serverService.findPublicIp(serverRef);

```


Modify public IP functionality
------------------------------

``` java

/* 
 * this methods modify all public IPs of target servers (in 90% cases it will be one IP)
 */
 
serverService
    .modifyPublicIp(serverRef,
        new PublicIpConfig()
            .openPorts(PortConfig.HTTPS, PortConfig.HTTP)
            .sourceRestrictions("70.100.60.140/32")
    );
    
serverService
    .modifyPublicIp(
        asList(
            serverRef1, serverRef2
        ),
        new PublicIpConfig()
            .openPorts(PortConfig.HTTPS, PortConfig.HTTP)
            .sourceRestrictions("70.100.60.140/32")
    );
    
serverService
    .modifyPublicIp(
        new ServerFilter()
            .dataCenters(DataCenters.US_WEST_SEATTLE)
            .onlyActive(),
        new PublicIpConfig()
            .openPorts(PortConfig.HTTPS, PortConfig.HTTP)
            .sourceRestrictions("70.100.60.140/32")
    );
    
/*
 * Method for public IP modification in case of multiple IPs
 */
serverService
    .modifyPublicIp(
        serverRef, publicIP, 
        new PublicIpConfig()
            .openPorts(PortConfig.HTTPS, PortConfig.HTTP)
            .sourceRestrictions("70.100.60.140/32")
    );

```


Delete public IP functionality
------------------------------

``` java

// remove all public IP of specified server
serverService.removePublicIp(serverRef);

// remove specified public IP of some server
serverService.removePublicIp(serverRef, publicIp);

```


Create group functionality
--------------------------

``` java

groupService
    .create(new GroupConfig()
        .name(newGroupName)
        .description(newGroupDescription)
        .parentGroup(Group.refByName()
            .dataCenter(DataCenters.DE_FRANKFURT)
            .name(DefaultGroups.DEFAULT_GROUP)
        )
    )

```


Modify Group Functionality
--------------------------

``` java

groupService
    .update(groupRef, new GroupConfig()
        .name(groupName)
        .description(groupDescription)
        .parentGroup(Group.refById()
            .dataCenter(DataCenters.DE_FRANKFURT)
            .id(parentGroupId)
        )
    )
    .waitUntilComplete()
        
```


Delete Group Functionality
--------------------------

``` java

groupServer.delete(groupRef);

```







Search DataCenters Functionality
----------------------------------

Implemented:

1. Case insensitive search by ID 

``` java

List<DataCenterMetadata> results = 
    dataCenterService
        .find(new DataCenterFilter()
            .dataCenters(dataCenterRef)
            .id("VA1", "ca1")
            .nameContains("FrankFurt")
            .where(d -> d.getGroup().equals("groupId"))
        );

```


Search Groups Functionality
-----------------------------

Implemented:

1. Possibilities to search by full group name match.

``` java

List<GroupMetadata> results = 
    groupServerService.find(
        new GroupFilter()
            .dataCenters(dataCenter1, dataCenter2)
            .name("MyGroup")
    )

```

Search Servers Functionality
-----------------------------

Implemented:

1. Possibilities to search by server status - **active**, **archived**, **underConstruction**
2. Possibilities to search by server power state - **started**, **paused**, **stopped**


``` java

serverService
    .find(new ServerFilter()
        .dataCenters(dataCenter1, dataCenter2)
        .status("active", "archived")
        .powerState(STARTED, PAUSED)
    );
    
```


Add public IP functionality
---------------------------

Implemented:

1. Possibilities to add public IP for multiple servers specified by reference
2. Possibilities to add public IP for multiple servers specified by server filter


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


Modify public IP functionality
------------------------------

Implemented:

1. Possibilities to modify public IP for multiple servers specified by reference
2. Possibilities to modify public IP for multiple servers specified by server filter

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


```


Delete public IP functionality
------------------------------

Implemented:

1. Possibilities to delete public IP by server reference
2. Possibilities to delete public IP by server filter

``` java

serverService.removePublicIp(serverRef1, serverRef2);

serverService.removePublicIp(new ServerFilter().status(ACTIVE));

```


Modify Group Functionality
--------------------------

Implemented:

1. Possibilities to modify multiple groups specified by references
2. Possibilities to modify multiple groups specified by group filter

``` java

groupService
    .update(asList(groupRef1, groupRef2), new GroupConfig()
        .name(groupName)
        .description(groupDescription)
        .parentGroup(Group.refById()
            .dataCenter(DataCenters.DE_FRANKFURT)
            .id(parentGroupId)
        )
    )
    .waitUntilComplete()
    
groupService
    .update(new GroupFilter().name("MyGroup"), new GroupConfig()
        .name(groupName)
        .description(groupDescription)
    )
    .waitUntilComplete()
        
```


Delete Group Functionality
--------------------------

Implemented:

1. Possibilities to delete multiple groups specified by references 
2. Possibilities to delete multiple groups specified by group filter 

``` java

groupServer.delete(groupRef1, groupRef2);

groupServer.delete(new GroupFilter().name("MyGroup"));

```


Server Operations
----------------------------

Single server operations:

``` java

private void powerOnServer() {
    serverService
        .powerOn(server)
        .waitUntilComplete();
}

private void powerOffServer() {
    serverService
        .powerOff(server)
        .waitUntilComplete();
}

private void pauseServer() {
    serverService
        .pause(server)
        .waitUntilComplete();
}

private void shutDownServer() {
    serverService
        .shutDown(server)
        .waitUntilComplete();
}

private void stopServerMaintenance() {
    serverService
        .stopMaintenance(server)
        .waitUntilComplete();
}

private void startServerMaintenance() {
    serverService
        .startMaintenance(server)
        .waitUntilComplete();
}

private void archiveServer() {
    serverService
        .archive(server)
        .waitUntilComplete();
}

private void createServerSnapshot() {
    serverService
        .createSnapshot(1, server)
        .waitUntilComplete();
}

private void restoreServer(GroupRef group) {
    serverService
        .restore(server, group)
        .waitUntilComplete();
}

```

Multiple servers operations:

``` java

protected void powerOn() {
    serverService
        .powerOn(new ServerFilter()
            .dataCenters(DE1_FRANKFURT)
            .groups("MyServer")
        )
        .waitUntilComplete();
}

protected void powerOff() {
    serverService
        .powerOff(new ServerFilter()
            .id(id1, id2)
        )
        .waitUntilComplete();
}

protected void pause() {
    serverService
        .pause(new ServerFilter()
            .id(id1, id2)
        )
        .waitUntilComplete();
}

protected void shutDown() {
    serverService
        .shutDown(new ServerFilter()
            .id(id1, id2)
        )
        .waitUntilComplete();
}

private final serverFilter =
    new ServerFilter()
        .id(
            serverMetadata1.getId(),
            serverMetadata2.getId()
        );

protected void stopServerMaintenance() {
    serverService
        .stopMaintenance(serverFilter)
        .waitUntilComplete();
}

protected void startServerMaintenance() {
    serverService
        .startMaintenance(serverFilter)
        .waitUntilComplete();
}

protected void archiveServer() {
    serverService
        .archive(serverFilter)
        .waitUntilComplete();
}

protected void createServerSnapshot() {
    serverService
        .createSnapshot(1, serverFilter)
        .waitUntilComplete();
}

protected void restoreServer(GroupRef group, ServerRef server) {
    serverService
        .restore(server, group)
        .waitUntilComplete();
}

protected void resetServer() {
    serverService
        .reset(serverFilter)
        .waitUntilComplete();
}

protected void rebootServer() {
    serverService
        .reboot(serverFilter)
        .waitUntilComplete();
}

```

Groups operations
-----------------

``` java

protected void powerOnServer() {
    serverService
        .powerOn(new GroupFilter()
            .dataCenters("va1", "ca1")
            .nameContains("MyServer")
        )
        .waitUntilComplete();
}

private final groupFilter =
    new GroupFilter()
        .dataCenter(DE1_FRANKFURT)
        .name("MyGroup");

protected void powerOffServer() {
    serverService
        .powerOff(groupFilter)
        .waitUntilComplete();
}

protected void pauseServer() {
    serverService
        .pause(groupFilter)
        .waitUntilComplete();
}

protected void shutDownServer() {
    serverService
        .shutDown(groupFilter)
        .waitUntilComplete();
}

protected void stopServerMaintenance() {
    serverService
        .stopMaintenance(groupFilter)
        .waitUntilComplete();
}

protected void startServerMaintenance() {
    serverService
        .startMaintenance(groupFilter)
        .waitUntilComplete();
}

protected void archiveServer() {
    serverService
        .archive(groupFilter)
        .waitUntilComplete();
}

protected void createServerSnapshot() {
    serverService
        .createSnapshot(1, groupFilter)
        .waitUntilComplete();
}

protected void restoreServer(GroupRef group, ServerRef server) {
    serverService
        .restore(server, group)
        .waitUntilComplete();
}

protected void resetServer() {
    serverService
        .reset(groupFilter)
        .waitUntilComplete();
}

protected void rebootServer() {
    serverService
        .reboot(groupFilter)
        .waitUntilComplete();
}

```


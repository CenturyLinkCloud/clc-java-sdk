
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

@Test
public void testFindTemplateByOsRef() throws Exception {
    TemplateMetadata metadata = templateService.findByRef(Template.refByOs()
        .dataCenter(US_EAST_STERLING)
        .type(RHEL)
        .version("6")
        .architecture(x86_64)
    );

    assertEquals(metadata.getName(), "RHEL-6-64-TEMPLATE");
}

@Test
public void testFindTemplateByNameRef() {
    TemplateMetadata metadata = templateService.findByRef(Template.refByName()
        .dataCenter(US_EAST_STERLING)
        .name("CENTOS-6-64-TEMPLATE")
    );

    assertEquals(metadata.getName(), "CENTOS-6-64-TEMPLATE");
}

@Test
public void testFindTemplateByDescriptionRef() {
    TemplateMetadata metadata = templateService.findByRef(Template.refByDescription()
        .dataCenter(US_EAST_STERLING)
        .description("pxe boot")
    );

    assertEquals(metadata.getName(), "PXE-TEMPLATE");
}

@Test
public void testFindAllCentOsTemplates() {
    List<TemplateMetadata> results = templateService.find(new TemplateFilter()
        .dataCenters(US_EAST_STERLING)
        .osTypes(new OsFilter()
            .type(CENTOS)
        )
    );

    assertEquals(results.size(), 2);
    assertEquals(
        map(results, TemplateMetadata::getName),
        asList("CENTOS-5-64-TEMPLATE", "CENTOS-6-64-TEMPLATE")
    );
}

@Test
public void testFindAllTemplatesWithManagedOsCapabilities() {
    List<TemplateMetadata> results = templateService.find(new TemplateFilter()
        .dataCenters(US_EAST_STERLING)
        .where(t -> t.getCapabilities().contains(MANAGED_OS_VALUE))
    );

    assertEquals(results.size(), 8);
}

```
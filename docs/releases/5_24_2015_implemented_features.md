

Remote execution of shell commands
----------------------------------

``` java

serverService
    .execSsh(serverRef)
    .run("mv ~/text ~/MyFolder/text")
    .execute();
    
```


Analytics Engine for Monitoring Statistics
------------------------------------------

``` java

List<MonitoringStatsEntry> results =
    statisticsService
        .monitoringStats()
        .forServers(new ServerFilter()
            .dataCenters(DE_FRANKFURT, CA_TORONTO)
            .descriptionContains("Cassandra")
        )
        .forTime(new SamplingConfig()
            .from(now().minusDaysOf(2))
            .to(now())
            .interval(minuteOf(10))
        )
        .groupByDataCenter();

List<MonitoringStatsEntry> results =
    statisticsService
        .monitoringStats()
        .forGroups(new GroupFilter()
            .dataCenters(DE_FRANKFURT, CA_TORONTO)
            .names("Application", "Hadoop Cluster")
        )
        .forTime(new SamplingConfig()
            .from(now().minusDaysOf(2))
            .interval(daysOf(1))
        )
        .summarize();

List<MonitoringStatsEntry> results =
    statisticsService
        .monitoringStats()
        .forDataCenters(new DataCenterFilter()
            .dataCenters(DE_FRANKFURT, CA_TORONTO)
        )
        .forTime(new SamplingConfig()
            .type(REALTIME)
        )
        .groupByServer();

List<MonitoringStatsEntry> results =
    statisticsService
        .monitoringStats()
        .forDataCenters(new DataCenterFilter()
            .dataCenters(DE_FRANKFURT, CA_TORONTO)
        )
        .forTime(new SamplingConfig()
            .last(daysOf(5))
        )
        .groupByServerGroup();

```
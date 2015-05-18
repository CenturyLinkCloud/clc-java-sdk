
Get Monitoring Statistics functionality
--------------------------------------------------

``` java

List<ServerMonitoringStats> result = 
    serverService
        .getMonitoringStats(group, new SamplingConfig()
            .from(now().minusDaysOf(2))
            .to(now())
            .interval(hoursOf(2))
        );

List<ServerMonitoringStats> result = 
    serverService
        .getMonitoringStats(
            asList(groups1, group2), 
            new SamplingConfig().type(REALTIME)
        );

List<ServerMonitoringStats> result = 
    serverService
        .getMonitoringStats(
            new GroupFilter()
                .dataCenters(DE_FRANKFURT, CA_TORONTO)
                .names("Application", "Hadoop Cluster"),

            new SamplingConfig()
                .from(now().minusDaysOf(2))
                .interval(daysOf(1))
        );

```

Get Billing Statistics functionality
--------------------------------------------------

``` java

GroupBillingStats result = serverService.getBillingStats(group);

List<GroupBillingStats> result = serverService.getBillingStats(asList(groups1, group2));

List<GroupBillingStats> result =
    serverService
        .getBillingStats(
            new GroupFilter()
                .dataCenters(DE_FRANKFURT, CA_TORONTO)
                .names("Application", "Hadoop Cluster")
        );

```

Analytics Engine for Billing Statistics
----------------------------------------

``` java

List<BillingStatsEntry> results =
    statisticsService
        .billingStats()
        .forServers(new ServerFilter()
            .dataCenters(DE_FRANKFURT, CA_TORONTO)
            .descriptionContains("Cassandra")
        )
        .groupByDataCenter();

List<BillingStatsEntry> results =
    statisticsService
        .billingStats()
        .forGroups(new GroupFilter()
            .dataCenters(DE_FRANKFURT, CA_TORONTO)
            .names("Application", "Hadoop Cluster")
        )
        .summarize();

List<BillingStatsEntry> results =
    statisticsService
        .billingStats()
        .forDataCenters(new DataCenterFilter()
            .dataCenters(DE_FRANKFURT, CA_TORONTO)
        )
        .groupByServer();

List<BillingStatsEntry> results =
    statisticsService
        .billingStats()
        .forDataCenters(new DataCenterFilter()
            .dataCenters(DE_FRANKFURT, CA_TORONTO)
        )
        .groupByServerGroup();
        
```

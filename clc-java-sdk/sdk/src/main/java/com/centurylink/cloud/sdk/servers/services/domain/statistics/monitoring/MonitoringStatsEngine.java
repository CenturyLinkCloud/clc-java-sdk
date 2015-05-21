package com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring;

import com.centurylink.cloud.sdk.common.management.services.DataCenterService;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.servers.client.domain.group.ServerMonitoringStatistics;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.group.ServerMonitoringFilter;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.filter.MonitoringStatsDataCenterFilter;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.filter.MonitoringStatsFilter;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.filter.MonitoringStatsGroupFilter;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.filter.MonitoringStatsServerFilter;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.grouping.GroupMonitoringStatsByDataCenter;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.grouping.GroupMonitoringStatsByGroup;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.grouping.GroupMonitoringStatsByServer;

import java.util.List;
import java.util.Map;

/**
 * @author aliaksandr.krasitski
 */
public class MonitoringStatsEngine {

    private final ServerService serverService;
    private final GroupService groupService;
    private final DataCenterService dataCenterService;

    private MonitoringStatsFilter statsFilter;

    private ServerMonitoringFilter timeFilter;

    private boolean aggregateSubItems = false;

    private String accountAlias;

    public MonitoringStatsEngine(ServerService serverService,
                             GroupService groupService,
                             DataCenterService dataCenterService,
                             String accountAlias) {
        this.serverService = serverService;
        this.groupService = groupService;
        this.dataCenterService = dataCenterService;
        this.accountAlias = accountAlias;
        initFilters();
    }

    private void initFilters() {
        this.timeFilter = new ServerMonitoringFilter();
    }

    public MonitoringStatsEngine aggregateSubItems() {
        aggregateSubItems = true;
        return this;
    }

    public MonitoringStatsEngine forServers(ServerFilter serverFilter) {
        this.statsFilter = new MonitoringStatsServerFilter(serverFilter, serverService);
        return this;
    }

    public MonitoringStatsEngine forGroups(GroupFilter groupFilter) {
        this.statsFilter = new MonitoringStatsGroupFilter(groupFilter);
        return this;
    }

    public MonitoringStatsEngine forDataCenters(DataCenterFilter dataCenterFilter) {
        this.statsFilter = new MonitoringStatsDataCenterFilter(dataCenterFilter);
        return this;
    }

    public MonitoringStatsEngine forTime(ServerMonitoringFilter monitoringConfig) {
        this.timeFilter = monitoringConfig;
        return this;
    }

    public List<MonitoringStatsEntry> groupByDataCenter() {
        return new GroupMonitoringStatsByDataCenter(serverService, dataCenterService, statsFilter)
            .group(getServerMonitoringStats());
    }

    public List<MonitoringStatsEntry> summarize() {
        return new GroupMonitoringStatsByServer(serverService, statsFilter)
            .summarize(getServerMonitoringStats(), accountAlias);
    }

    public List<MonitoringStatsEntry> groupByServer() {
        return new GroupMonitoringStatsByServer(serverService, statsFilter)
            .group(getServerMonitoringStats());
    }

    public List<MonitoringStatsEntry> groupByServerGroup() {
        return new GroupMonitoringStatsByGroup(groupService, serverService, statsFilter, aggregateSubItems)
            .group(getServerMonitoringStats());
    }

    private Map<Group, List<ServerMonitoringStatistics>> getServerMonitoringStats() {
        return groupService.getMonitoringStatsAsMap(statsFilter.getFilter(), timeFilter);
    }

}

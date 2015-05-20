package com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.grouping;

import com.centurylink.cloud.sdk.common.management.services.DataCenterService;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.servers.client.domain.group.ServerMonitoringStatistics;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.MonitoringEntry;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.MonitoringStatsEntry;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.filter.MonitoringStatsFilter;

import java.util.HashMap;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class GroupMonitoringStatsByDataCenter extends GroupMonitoringStatsBy {

    private ServerService serverService;
    private DataCenterService dataCenterService;

    public GroupMonitoringStatsByDataCenter(
        ServerService serverService,
        DataCenterService dataCenterService,
        MonitoringStatsFilter statsFilter
    ) {
        super(statsFilter);
        this.serverService = serverService;
        this.dataCenterService = dataCenterService;
    }

    @Override
    public List<MonitoringStatsEntry> group(List<ServerMonitoringStatistics> stats) {
        HashMap<String, List<MonitoringEntry>> plainGroupMap = new HashMap<>();

        stats.stream()
            .forEach(stat ->
                collectStats(plainGroupMap,
                    serverService.findByRef(Server.refById(stat.getName())).getLocationId(),
                    stat.getStats()
                )
            );

        return aggregate(convertToMonitoringEntries(plainGroupMap));
    }

    private List<MonitoringStatsEntry> convertToMonitoringEntries(
        HashMap<String, List<MonitoringEntry>> plainGroupMap) {
        return plainGroupMap.keySet().stream()
            .map(key -> createMonitoringStatsEntry(
                dataCenterService.findByRef(DataCenter.refById(key)),
                plainGroupMap.get(key))
            )
            .collect(toList());
    }
}

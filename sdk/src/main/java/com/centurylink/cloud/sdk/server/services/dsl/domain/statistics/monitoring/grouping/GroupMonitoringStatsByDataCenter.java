package com.centurylink.cloud.sdk.server.services.dsl.domain.statistics.monitoring.grouping;

import com.centurylink.cloud.sdk.base.services.dsl.DataCenterService;
import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.server.services.client.domain.group.ServerMonitoringStatistics;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.refs.Server;
import com.centurylink.cloud.sdk.server.services.dsl.domain.statistics.monitoring.MonitoringEntry;
import com.centurylink.cloud.sdk.server.services.dsl.domain.statistics.monitoring.MonitoringStatsEntry;
import com.centurylink.cloud.sdk.server.services.dsl.domain.statistics.monitoring.filter.MonitoringStatsFilter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.centurylink.cloud.sdk.core.services.SdkThreadPool.executeParallel;
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
    public List<MonitoringStatsEntry> group(Map<Group, List<ServerMonitoringStatistics>> stats) {
        Map<String, List<MonitoringEntry>> plainGroupMap = new HashMap<>();

        executeParallel(
            selectServersStatsDistinct(stats).stream(),
            stat ->
                collectStats(plainGroupMap,
                    serverService.findByRef(Server.refById(stat.getName())).getLocationId(),
                    stat.getStats(),
                    false
                )
        );

        return aggregate(convertToMonitoringEntries(plainGroupMap));
    }

    private List<MonitoringStatsEntry> convertToMonitoringEntries(Map<String, List<MonitoringEntry>> plainGroupMap) {
        return executeParallel(plainGroupMap.keySet().stream()
            .map(key -> createMonitoringStatsEntry(
                dataCenterService.findByRef(DataCenter.refById(key)),
                plainGroupMap.get(key))
            )
        );
    }
}

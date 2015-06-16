package com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.grouping;

import com.centurylink.cloud.sdk.core.function.Predicates;
import com.centurylink.cloud.sdk.servers.client.domain.group.ServerMonitoringStatistics;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.account.AccountMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.MonitoringEntry;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.MonitoringStatsEntry;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.filter.MonitoringStatsFilter;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.filter.MonitoringStatsServerFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

public class GroupMonitoringStatsByServer extends GroupMonitoringStatsBy {

    private ServerService serverService;

    private Predicate<ServerMonitoringStatistics> filterServers() {
        if (statsFilter instanceof MonitoringStatsServerFilter) {
            List<String> serverIdsRestrictions = ((MonitoringStatsServerFilter)statsFilter).getServerIdRestrictionsList();
            return (stat -> serverIdsRestrictions.isEmpty() || serverIdsRestrictions.contains(stat.getName()));
        }
        return Predicates.alwaysTrue();
    }

    public GroupMonitoringStatsByServer(ServerService serverService, MonitoringStatsFilter statsFilter) {
        super(statsFilter);
        this.serverService = serverService;
    }

    @Override
    public List<MonitoringStatsEntry> group(Map<Group, List<ServerMonitoringStatistics>> stats) {
        Map<String, List<MonitoringEntry>> plainGroupMap = new HashMap<>();
        selectServersStatsDistinct(stats).stream()
            .filter(filterServers())
            .forEach(stat ->
                    collectStats(plainGroupMap,
                        stat.getName(),
                        stat.getStats(),
                        true
                    )
            );

        return aggregate(convertToMonitoringEntries(plainGroupMap));
    }

    public List<MonitoringStatsEntry> summarize(Map<Group, List<ServerMonitoringStatistics>> monitoringStatsMap,
                                                   String accountAlias) {
        List<MonitoringEntry> monitoringEntries = monitoringStatsMap.values().stream()
            .flatMap(List::stream)
            .filter(filterServers())
            .map(stat -> convertEntry(stat.getStats()))
            .flatMap(List::stream)
            .collect(toList());

        return aggregate(Arrays.asList(createMonitoringStatsEntry(
            new AccountMetadata(accountAlias),
            monitoringEntries
        )));
    }

    private List<MonitoringStatsEntry> convertToMonitoringEntries(Map<String, List<MonitoringEntry>> plainGroupMap) {

        List<MonitoringStatsEntry> result = new ArrayList<>();
        plainGroupMap.forEach(
            (key, statistics) -> result.add(
                createMonitoringStatsEntry(serverService.findByRef(Server.refById(key)), statistics)
            )
        );

        return result;
    }
}

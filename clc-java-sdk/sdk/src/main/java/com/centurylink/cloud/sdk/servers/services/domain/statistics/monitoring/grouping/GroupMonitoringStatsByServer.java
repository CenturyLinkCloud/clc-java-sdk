package com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.grouping;

import com.centurylink.cloud.sdk.servers.client.domain.group.ServerMonitoringStatistics;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.account.AccountMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.MonitoringEntry;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.MonitoringStatsEntry;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.filter.MonitoringStatsFilter;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.filter.MonitoringStatsServerFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

public class GroupMonitoringStatsByServer extends GroupMonitoringStatsBy {

    private ServerService serverService;

    private Predicate<ServerMonitoringStatistics> filterServers() {
        List<String> serverIdsRestrictions = ((MonitoringStatsServerFilter)statsFilter).getServerIdRestrictionsList();
        return (stat -> serverIdsRestrictions.isEmpty() || serverIdsRestrictions.contains(stat.getName()));
    }

    public GroupMonitoringStatsByServer(ServerService serverService, MonitoringStatsFilter statsFilter) {
        super(statsFilter);
        this.serverService = serverService;
    }

    @Override
    public List<MonitoringStatsEntry> group(List<ServerMonitoringStatistics> stats) {
        HashMap<String, List<MonitoringEntry>> plainGroupMap = new HashMap<>();
        stats.stream()
            .filter(filterServers())
            .forEach(stat ->
                collectStats(plainGroupMap,
                    stat.getName(),
                    stat.getStats()
                )
            );

        return aggregate(convertToMonitoringEntries(plainGroupMap));
    }

    public List<MonitoringStatsEntry> summarize(List<ServerMonitoringStatistics> monitoringStatsList,
                                                   String accountAlias) {
        List<MonitoringEntry> monitoringEntries = monitoringStatsList.stream()
            .filter(filterServers())
            .map(stat -> convertEntry(stat.getStats()))
            .flatMap(List::stream)
            .collect(toList());

        return aggregate(Arrays.asList(createMonitoringStatsEntry(
            new AccountMetadata(accountAlias),
            monitoringEntries
        )));
    }

    private List<MonitoringStatsEntry> convertToMonitoringEntries(
        HashMap<String, List<MonitoringEntry>> plainGroupMap) {

        List<MonitoringStatsEntry> result = new ArrayList<>();
        plainGroupMap.forEach(
            (key, statistics) -> result.add(
                createMonitoringStatsEntry(serverService.findByRef(Server.refById(key)), statistics)
            )
        );

        return result;
    }
}

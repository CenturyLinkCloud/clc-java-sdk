package com.centurylink.cloud.sdk.server.services.dsl.domain.statistics.monitoring.grouping;

import com.centurylink.cloud.sdk.core.function.Predicates;
import com.centurylink.cloud.sdk.server.services.client.domain.group.ServerMonitoringStatistics;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.account.AccountMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.refs.Server;
import com.centurylink.cloud.sdk.server.services.dsl.domain.statistics.monitoring.MonitoringEntry;
import com.centurylink.cloud.sdk.server.services.dsl.domain.statistics.monitoring.MonitoringStatsEntry;
import com.centurylink.cloud.sdk.server.services.dsl.domain.statistics.monitoring.filter.MonitoringStatsFilter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.statistics.monitoring.filter.MonitoringStatsServerFilter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static com.centurylink.cloud.sdk.core.services.SdkThreadPool.executeParallel;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class GroupMonitoringStatsByServer extends GroupMonitoringStatsBy {

    private ServerService serverService;

    public GroupMonitoringStatsByServer(ServerService serverService, MonitoringStatsFilter statsFilter) {
        super(statsFilter);
        this.serverService = serverService;
    }

    private Predicate<ServerMonitoringStatistics> filterServers() {
        if (statsFilter instanceof MonitoringStatsServerFilter) {
            List<String> serverIdsRestrictions = ((MonitoringStatsServerFilter)statsFilter).getServerIdRestrictionsList();
            return (stat -> serverIdsRestrictions.isEmpty() || serverIdsRestrictions.contains(stat.getName()));
        }
        return Predicates.alwaysTrue();
    }

    @Override
    public List<MonitoringStatsEntry> group(Map<Group, List<ServerMonitoringStatistics>> stats) {
        Map<String, List<MonitoringEntry>> plainGroupMap = new HashMap<>();

        selectServersStatsDistinct(stats)
            .stream()
            .filter(filterServers())
            .forEach(stat -> collectStats(
                plainGroupMap,
                stat.getName(),
                stat.getStats(),
                true
            ));

        return aggregate(convertToMonitoringEntries(plainGroupMap));
    }

    public List<MonitoringStatsEntry> summarize(Map<Group, List<ServerMonitoringStatistics>> monitoringStatsMap,
                                                   String accountAlias) {
        List<MonitoringEntry> monitoringEntries =
            monitoringStatsMap
                .values()
                .stream()
                .flatMap(List::stream)
                .filter(filterServers())
                .map(stat -> convertEntry(stat.getStats()))
                .flatMap(List::stream)
                .collect(toList());

        return aggregate(asList(createMonitoringStatsEntry(
            new AccountMetadata(accountAlias),
            monitoringEntries
        )));
    }

    private List<MonitoringStatsEntry> convertToMonitoringEntries(Map<String, List<MonitoringEntry>> plainGroupMap) {
        return executeParallel(
            plainGroupMap
                .entrySet()
                .stream()
                .map(entry ->
                    createMonitoringStatsEntry(
                        serverService.findByRef(Server.refById(entry.getKey())),
                        entry.getValue()
                    )
                )
        );
    }
}

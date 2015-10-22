package com.centurylink.cloud.sdk.server.services.dsl.domain.statistics.monitoring.grouping;

import com.centurylink.cloud.sdk.server.services.client.domain.group.ServerMonitoringStatistics;
import com.centurylink.cloud.sdk.server.services.dsl.GroupService;
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

public class GroupMonitoringStatsByGroup extends GroupMonitoringStatsBy {

    private GroupService groupService;
    private ServerService serverService;
    private final boolean aggregateSubItems;

    public GroupMonitoringStatsByGroup(GroupService groupService,
                                       ServerService serverService,
                                       MonitoringStatsFilter statsFilter,
                                       boolean aggregateSubItems) {
        super(statsFilter);
        this.groupService = groupService;
        this.serverService = serverService;
        this.aggregateSubItems = aggregateSubItems;
    }

    @Override
    public List<MonitoringStatsEntry> group(Map<Group, List<ServerMonitoringStatistics>> stats) {
        Map<String, List<MonitoringEntry>> plainGroupMap = new HashMap<>();

        stats.forEach((group, statsList) ->
            executeParallel(statsList.stream(),
                stat -> {
                    String key = serverService.findByRef(Server.refById(stat.getName())).getGroupId();

                    if (group.asFilter().getIds().contains(key) || aggregateSubItems) {
                        collectStats(plainGroupMap,
                            key,
                            stat.getStats(),
                            false
                        );
                    }
                }
            )
        );

        return aggregate(convertToMonitoringEntries(plainGroupMap));
    }

    private List<MonitoringStatsEntry> convertToMonitoringEntries(Map<String, List<MonitoringEntry>> plainGroupMap) {
        return executeParallel(
            plainGroupMap.entrySet().stream().map(
                entry ->
                    createMonitoringStatsEntry(
                        groupService.findByRef(Group.refById(entry.getKey())),
                        entry.getValue()
                    )
            )
        );
    }
}

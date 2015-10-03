package com.centurylink.cloud.sdk.server.services.dsl.domain.statistics.monitoring.filter;

import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.filters.ServerFilter;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class MonitoringStatsServerFilter implements MonitoringStatsFilter {

    private List<ServerMetadata> serverMetadataList;
    private List<String> serverIdRestrictionsList;

    public MonitoringStatsServerFilter(ServerFilter serverFilter, ServerService serverService) {
        serverMetadataList = serverService.find(serverFilter);

        serverIdRestrictionsList = serverMetadataList
            .stream()
            .map(ServerMetadata::getId)
            .collect(toList());
    }

    @Override
    public GroupFilter getFilter() {
        return
            new GroupFilter().id(
                serverMetadataList
                    .stream()
                    .map(ServerMetadata::getGroupId)
                    .distinct()
                    .collect(toList())
            );
    }

    public List<String> getServerIdRestrictionsList() {
        return serverIdRestrictionsList;
    }
}

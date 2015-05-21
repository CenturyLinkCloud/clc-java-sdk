package com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.filter;

import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;

public class MonitoringStatsGroupFilter implements MonitoringStatsFilter {

    private GroupFilter groupFilter;

    public MonitoringStatsGroupFilter(GroupFilter groupFilter) {
        this.groupFilter = groupFilter;
    }

    @Override
    public GroupFilter getFilter() {
        return groupFilter;
    }
}

package com.centurylink.cloud.sdk.server.services.dsl.domain.statistics.monitoring.filter;

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.filters.GroupFilter;

public class MonitoringStatsDataCenterFilter implements MonitoringStatsFilter {

    private DataCenterFilter dataCenterFilter;

    public MonitoringStatsDataCenterFilter(DataCenterFilter dataCenterFilter) {
        this.dataCenterFilter = dataCenterFilter;
    }

    @Override
    public GroupFilter getFilter() {
        return
            new GroupFilter()
                .dataCentersWhere(dataCenterFilter);
    }
}

package com.centurylink.cloud.sdk.servers.services.domain.statistics.billing.filter;

import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;

public class BillingStatsDataCenterFilter implements BillingStatsFilter {

    private DataCenterFilter dataCenterFilter;

    public BillingStatsDataCenterFilter(DataCenterFilter dataCenterFilter) {
        this.dataCenterFilter = dataCenterFilter;
    }

    @Override
    public GroupFilter getFilter() {
        return
            new GroupFilter()
                .dataCentersWhere(dataCenterFilter);
    }
}

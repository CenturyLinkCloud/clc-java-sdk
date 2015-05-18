package com.centurylink.cloud.sdk.servers.services.domain.statistics.billing.filter;

import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;

public class BillingStatsGroupFilter implements BillingStatsFilter {

    private GroupFilter groupFilter;

    public BillingStatsGroupFilter(GroupFilter groupFilter) {
        this.groupFilter = groupFilter;
    }

    @Override
    public GroupFilter getFilter() {
        return groupFilter;
    }
}

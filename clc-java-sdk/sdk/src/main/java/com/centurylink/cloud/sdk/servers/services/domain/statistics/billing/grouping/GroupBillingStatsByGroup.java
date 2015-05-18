package com.centurylink.cloud.sdk.servers.services.domain.statistics.billing.grouping;

import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.domain.group.BillingStats;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.billing.BillingStatsEntry;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.billing.Statistics;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.billing.filter.BillingStatsFilter;

import java.util.ArrayList;
import java.util.List;

public class GroupBillingStatsByGroup extends GroupBillingStatsBy {

    private GroupService groupService;

    public GroupBillingStatsByGroup(GroupService groupService, BillingStatsFilter statsFilter) {
        super(statsFilter);
        this.groupService = groupService;
    }

    public List<BillingStatsEntry> group(List<BillingStats> billingStatsList) {
        List<BillingStatsEntry> result = new ArrayList<>();

        billingStatsList.forEach(
            billingStats ->
                billingStats.getGroups().forEach(
                    groupBilling -> {
                        Statistics statistics = new Statistics();
                        aggregateStats(statistics, groupBilling);

                        result.add(
                            createBillingStatsEntry(
                                groupService.findByRef(
                                    Group.refById(groupBilling.getGroupId())
                                ),
                                statistics
                            )
                        );
                    }
                )
        );

        return result;
    }
}

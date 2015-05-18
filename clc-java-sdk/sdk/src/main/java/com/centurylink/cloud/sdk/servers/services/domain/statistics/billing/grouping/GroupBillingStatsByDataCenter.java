package com.centurylink.cloud.sdk.servers.services.domain.statistics.billing.grouping;

import com.centurylink.cloud.sdk.common.management.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.common.management.services.DataCenterService;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.domain.group.BillingStats;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.billing.BillingStatsEntry;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.billing.Statistics;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.billing.filter.BillingStatsFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupBillingStatsByDataCenter extends GroupBillingStatsBy {

    private GroupService groupService;
    private DataCenterService dataCenterService;

    public GroupBillingStatsByDataCenter(
            GroupService groupService,
            DataCenterService dataCenterService,
            BillingStatsFilter statsFilter
    ) {
        super(statsFilter);
        this.groupService = groupService;
        this.dataCenterService = dataCenterService;
    }


    public List<BillingStatsEntry> group(List<BillingStats> billingStatsList) {
        List<BillingStatsEntry> result = new ArrayList<>();

        Map<DataCenterMetadata, Statistics> dataCenterMap = new HashMap<>();

        billingStatsList.forEach(
            billingStats -> billingStats.getGroups().forEach(
                groupBilling -> {
                    GroupMetadata groupMetadata = groupService.findByRef(
                            Group.refById(groupBilling.getGroupId())
                    );

                    DataCenterMetadata dataCenterMetadata = dataCenterService.findByRef(
                            DataCenter.refById(groupMetadata.getLocationId())
                    );

                    if (dataCenterMap.get(dataCenterMetadata) != null) {
                        aggregateStats(dataCenterMap.get(dataCenterMetadata), groupBilling);
                    } else {
                        Statistics statistics = new Statistics();
                        aggregateStats(statistics, groupBilling);
                        dataCenterMap.put(dataCenterMetadata, statistics);
                    }
                }
            )
        );

        dataCenterMap.forEach(
            (dataCenterMetadata, statistics) -> result.add(
                createBillingStatsEntry(dataCenterMetadata, statistics)
            )
        );

        return result;
    }
}

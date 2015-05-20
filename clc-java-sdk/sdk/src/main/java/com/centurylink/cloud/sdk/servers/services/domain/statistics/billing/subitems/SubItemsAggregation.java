package com.centurylink.cloud.sdk.servers.services.domain.statistics.billing.subitems;

import com.centurylink.cloud.sdk.servers.services.domain.group.BillingStats;
import com.centurylink.cloud.sdk.servers.services.domain.group.GroupBilling;

import java.util.ArrayList;
import java.util.List;

public enum SubItemsAggregation {

    WITH(true),
    WITHOUT(false);

    private boolean flag;

    SubItemsAggregation(boolean flag) {
        this.flag = flag;
    }

    public List<BillingStats> aggregate(List<BillingStats> billingStatsList) {
        if (!flag) {
            billingStatsList.forEach(
                billingStats -> {
                    GroupBilling groupBilling = billingStats
                        .getGroups()
                        .stream()
                        .findFirst()
                        .orElse(new GroupBilling());

                    List<GroupBilling> groupBillingList = new ArrayList<>();
                    groupBillingList.add(groupBilling);

                    billingStats.setGroups(groupBillingList);
                }
            );
        }

        return billingStatsList;
    }
}

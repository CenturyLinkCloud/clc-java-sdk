/*
 * (c) 2015 CenturyLink. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

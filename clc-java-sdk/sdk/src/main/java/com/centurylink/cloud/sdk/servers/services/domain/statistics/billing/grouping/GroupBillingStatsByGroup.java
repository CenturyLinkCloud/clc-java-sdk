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

    @Override
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

/*
 * (c) 2015 CenturyLink Cloud. All Rights Reserved.
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

import com.centurylink.cloud.sdk.servers.services.domain.group.BillingStats;
import com.centurylink.cloud.sdk.servers.services.domain.group.GroupBilling;
import com.centurylink.cloud.sdk.servers.services.domain.group.ServerBilling;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.billing.BillingStatsEntry;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.billing.Statistics;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.billing.filter.BillingStatsFilter;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.billing.filter.BillingStatsServerFilter;

import java.util.List;

public abstract class GroupBillingStatsBy {

    protected BillingStatsFilter statsFilter;

    public abstract List<BillingStatsEntry> group(List<BillingStats> billingStatsList);

    public GroupBillingStatsBy(BillingStatsFilter statsFilter) {
        this.statsFilter = statsFilter;
    }

    protected void aggregateStats(Statistics statistics, GroupBilling groupBilling) {
        groupBilling.getServers().forEach(
            serverBilling -> {
                if (checkServerId(serverBilling)) {
                    aggregateStats(statistics, serverBilling);
                }
            }
        );
    }

    protected void aggregateStats(Statistics statistics, ServerBilling serverBilling) {
        statistics
            .archiveCost(
                statistics.getArchiveCost().add(serverBilling.getArchiveCost())
            )
            .templateCost(
                statistics.getTemplateCost().add(serverBilling.getTemplateCost())
            )
            .monthlyEstimate(
                statistics.getMonthlyEstimate().add(serverBilling.getMonthlyEstimate())
            )
            .monthToDate(
                statistics.getMonthToDate().add(serverBilling.getMonthToDate())
            )
            .currentHour(
                statistics.getCurrentHour().add(serverBilling.getCurrentHour())
            );
    }

    @SuppressWarnings("unchecked")
    protected <T> BillingStatsEntry createBillingStatsEntry(T metadata, Statistics statistics) {
        return new BillingStatsEntry()
                .entity(metadata)
                .statistics(statistics);
    }

    protected boolean checkServerId(ServerBilling serverBilling) {
        return !(statsFilter instanceof BillingStatsServerFilter)
                || ((BillingStatsServerFilter) statsFilter)
                .getServerIdRestrictionsList()
                .contains(serverBilling.getServerId());
    }

    public Statistics summarize(List<BillingStats> billingStatsList) {
        Statistics statistics = new Statistics();

        billingStatsList.forEach(
            billingStats -> billingStats.getGroups().forEach(
                groupBilling -> aggregateStats(statistics, groupBilling)
            )
        );

        return statistics;
    }
}

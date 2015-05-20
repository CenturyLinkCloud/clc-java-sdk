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

import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.group.BillingStats;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.billing.BillingStatsEntry;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.billing.Statistics;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.billing.filter.BillingStatsFilter;

import java.util.ArrayList;
import java.util.List;

public class GroupBillingStatsByServer extends GroupBillingStatsBy {

    private ServerService serverService;

    public GroupBillingStatsByServer(ServerService serverService, BillingStatsFilter statsFilter) {
        super(statsFilter);
        this.serverService = serverService;
    }

    @Override
    public List<BillingStatsEntry> group(List<BillingStats> billingStatsList) {
        List<BillingStatsEntry> result = new ArrayList<>();

        billingStatsList.forEach(
            billingStats ->
                billingStats.getGroups().forEach(
                    groupBilling -> groupBilling.getServers().forEach(
                        serverBilling -> {
                            if (checkServerId(serverBilling)) {
                                Statistics statistics = new Statistics();
                                aggregateStats(statistics, serverBilling);

                                result.add(
                                    createBillingStatsEntry(
                                        serverService.findByRef(
                                            Server.refById(serverBilling.getServerId())
                                        ),
                                        statistics
                                    )
                                );
                            }
                        }
                    )
                )
        );

        return result;
    }
}

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

package com.centurylink.cloud.sdk.server.services.dsl.domain.statistics.billing.grouping;

import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.BillingStats;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.GroupBilling;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.refs.Server;
import com.centurylink.cloud.sdk.server.services.dsl.domain.statistics.billing.BillingStatsEntry;
import com.centurylink.cloud.sdk.server.services.dsl.domain.statistics.billing.Statistics;
import com.centurylink.cloud.sdk.server.services.dsl.domain.statistics.billing.filter.BillingStatsFilter;

import java.util.List;

import static com.centurylink.cloud.sdk.core.function.Predicates.notNull;
import static com.centurylink.cloud.sdk.core.services.SdkThreadPool.executeParallel;

public class GroupBillingStatsByServer extends GroupBillingStatsBy {

    private ServerService serverService;

    public GroupBillingStatsByServer(ServerService serverService, BillingStatsFilter statsFilter) {
        super(statsFilter);
        this.serverService = serverService;
    }

    @Override
    public List<BillingStatsEntry> group(List<BillingStats> billingStatsList) {

        return executeParallel(
            billingStatsList.stream()
                .map(BillingStats::getGroups)
                .flatMap(List::stream)
                .map(GroupBilling::getServers)
                .flatMap(List::stream)
                .map(
                    serverBilling -> {
                        if (checkServerId(serverBilling)) {
                            Statistics statistics = new Statistics();
                            aggregateStats(statistics, serverBilling);

                            return
                                createBillingStatsEntry(
                                    serverService.findByRef(
                                        Server.refById(serverBilling.getServerId())
                                    ),
                                    statistics
                                );
                        }
                        return null;
                    }
                )
                .filter(notNull())
            );
    }
}

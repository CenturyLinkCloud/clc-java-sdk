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

package com.centurylink.cloud.sdk.servers.services.domain.statistics.billing;

import com.centurylink.cloud.sdk.common.management.services.DataCenterService;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.group.BillingStats;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.billing.filter.BillingStatsDataCenterFilter;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.billing.filter.BillingStatsFilter;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.billing.filter.BillingStatsGroupFilter;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.billing.filter.BillingStatsServerFilter;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.billing.grouping.GroupBillingStatsByDataCenter;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.billing.grouping.GroupBillingStatsByGroup;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.billing.grouping.GroupBillingStatsByServer;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.billing.subitems.SubItemsAggregation;

import java.util.ArrayList;
import java.util.List;

public class BillingStatsEngine {

    private BillingStatsFilter statsFilter = new BillingStatsGroupFilter(new GroupFilter());

    private SubItemsAggregation subItemsAggregation = SubItemsAggregation.WITHOUT;

    private final GroupService groupService;
    private final DataCenterService dataCenterService;
    private final ServerService serverService;

    public BillingStatsEngine(
            ServerService serverService,
            GroupService groupService,
            DataCenterService dataCenterService
    ) {
        this.serverService = serverService;
        this.groupService = groupService;
        this.dataCenterService = dataCenterService;
    }

    public BillingStatsEngine forServers(ServerFilter serverFilter) {
        this.statsFilter = new BillingStatsServerFilter(serverFilter, serverService);
        return this;
    }

    public BillingStatsEngine forGroups(GroupFilter groupFilter) {
        this.statsFilter = new BillingStatsGroupFilter(groupFilter);
        return this;
    }

    public BillingStatsEngine forDataCenters(DataCenterFilter dataCenterFilter) {
        this.statsFilter = new BillingStatsDataCenterFilter(dataCenterFilter);
        return this;
    }

    public List<BillingStatsEntry> groupByGroup() {
        return
            new GroupBillingStatsByGroup(groupService, statsFilter)
                .group(getBillingStats());
    }

    public List<BillingStatsEntry> groupByServer() {
        return
            new GroupBillingStatsByServer(serverService, statsFilter)
                .group(getBillingStats());
    }

    public List<BillingStatsEntry> groupByDataCenter() {
        return
            new GroupBillingStatsByDataCenter(groupService, dataCenterService, statsFilter)
                .group(getBillingStats());
    }

    public Statistics summarize() {
        return
            new GroupBillingStatsByGroup(groupService, statsFilter)
                .summarize(getBillingStats());
    }

    public BillingStatsEngine aggregateSubItems() {
        subItemsAggregation = SubItemsAggregation.WITH;
        return this;
    }

    private List<BillingStats> getBillingStats() {
        if (statsFilter instanceof BillingStatsServerFilter
                && ((BillingStatsServerFilter) statsFilter)
                    .getServerIdRestrictionsList()
                    .isEmpty()
        ) {
            return new ArrayList<>();
        }

        return subItemsAggregation.aggregate(
            groupService.getBillingStats(
                statsFilter.getFilter()
            )
        );
    }
}

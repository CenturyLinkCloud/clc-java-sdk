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

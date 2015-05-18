package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.common.management.services.DataCenterService;
import com.centurylink.cloud.sdk.core.auth.services.BearerAuthentication;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.billing.BillingStatsEngine;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.MonitoringStatsEngine;
import com.google.inject.Inject;

/**
 * @author Ilya Drabenia
 */
public class StatisticsService {

    private final ServerService serverService;
    private final GroupService groupService;
    private final DataCenterService dataCenterService;

    private String accountAlias;

    @Inject
    public StatisticsService(
            ServerService serverService,
            GroupService groupService,
            DataCenterService dataCenterService,
            BearerAuthentication authentication
    ) {
        this.serverService = serverService;
        this.groupService = groupService;
        this.dataCenterService = dataCenterService;
        this.accountAlias = authentication.getAccountAlias();
    }

    public BillingStatsEngine billingStats() {
        return new BillingStatsEngine(serverService, groupService, dataCenterService);
    }

    public MonitoringStatsEngine monitoringStats() {
        return new MonitoringStatsEngine(serverService, groupService, dataCenterService, accountAlias);
    }


}

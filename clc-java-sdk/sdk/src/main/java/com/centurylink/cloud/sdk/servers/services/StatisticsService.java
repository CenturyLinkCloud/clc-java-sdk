package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.common.management.services.DataCenterService;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.billing.BillingStatsEngine;
import com.google.inject.Inject;

/**
 * @author Ilya Drabenia
 */
public class StatisticsService {

    private final ServerService serverService;
    private final GroupService groupService;
    private final DataCenterService dataCenterService;

    @Inject
    public StatisticsService(
            ServerService serverService,
            GroupService groupService,
            DataCenterService dataCenterService
    ) {
        this.serverService = serverService;
        this.groupService = groupService;
        this.dataCenterService = dataCenterService;
    }

    public BillingStatsEngine billingStats() {
        return new BillingStatsEngine(serverService, groupService, dataCenterService);
    }

}

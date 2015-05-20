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

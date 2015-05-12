package com.centurylink.cloud.sdk.servers.services;

import com.google.inject.Inject;

/**
 * @author Ilya Drabenia
 */
public class StatisticsService {
    private final ServerService serverService;

    @Inject
    public StatisticsService(ServerService serverService) {
        this.serverService = serverService;
    }


}

package com.centurylink.cloud.sdk.servers.client.domain.group;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * @author aliaksandr.krasitski
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServerMonitoringMetadata {
    private String name;
    private List<ServerMonitoringStats> stats;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ServerMonitoringStats> getStats() {
        return stats;
    }

    public void setStats(List<ServerMonitoringStats> stats) {
        this.stats = stats;
    }
}

package com.centurylink.cloud.sdk.servers.client.domain.group;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * @author aliaksandr.krasitski
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServerMonitoringStatistics {
    private String name;
    private List<SamplingEntry> stats;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SamplingEntry> getStats() {
        return stats;
    }

    public void setStats(List<SamplingEntry> stats) {
        this.stats = stats;
    }
}

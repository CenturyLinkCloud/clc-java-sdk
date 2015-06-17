package com.centurylink.cloud.sdk.server.services.dsl.domain.statistics.monitoring;


/**
 * @author aliaksandr.krasitski
 */
public class GuestUsage {
    private String path;
    private Integer capacityMB;
    private Integer consumedMB;

    public String getPath() {
        return path;
    }

    public GuestUsage path(String path) {
        this.path = path;
        return this;
    }

    public Integer getCapacityMB() {
        return capacityMB;
    }

    public GuestUsage capacityMB(Integer capacityMB) {
        this.capacityMB = capacityMB;
        return this;
    }

    public Integer getConsumedMB() {
        return consumedMB;
    }

    public GuestUsage consumedMB(Integer consumedMB) {
        this.consumedMB = consumedMB;
        return this;
    }
}

package com.centurylink.cloud.sdk.server.services.dsl.domain.statistics.monitoring;

/**
 * @author aliaksandr.krasitski
 */
public class DiskUsage {
    private String id;
    private Integer capacityMB;

    public Integer getCapacityMB() {
        return capacityMB;
    }

    public DiskUsage capacityMB(Integer capacityMB) {
        this.capacityMB = capacityMB;
        return this;
    }

    public String getId() {
        return id;
    }

    public DiskUsage id(String id) {
        this.id = id;
        return this;
    }
}

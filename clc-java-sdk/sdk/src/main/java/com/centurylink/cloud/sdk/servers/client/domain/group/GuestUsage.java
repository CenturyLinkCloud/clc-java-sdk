package com.centurylink.cloud.sdk.servers.client.domain.group;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author aliaksandr.krasitski
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GuestUsage {
    private String path;
    private Integer capacityMB;
    private Integer consumedMB;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getCapacityMB() {
        return capacityMB;
    }

    public void setCapacityMB(Integer capacityMB) {
        this.capacityMB = capacityMB;
    }

    public Integer getConsumedMB() {
        return consumedMB;
    }

    public void setConsumedMB(Integer consumedMB) {
        this.consumedMB = consumedMB;
    }
}

package com.centurylink.cloud.sdk.servers.client.domain.group;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author aliaksandr.krasitski
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DiskUsage {
    private String id;
    private Integer capacityMB;

    public Integer getCapacityMB() {
        return capacityMB;
    }

    public void setCapacityMB(Integer capacityMB) {
        this.capacityMB = capacityMB;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

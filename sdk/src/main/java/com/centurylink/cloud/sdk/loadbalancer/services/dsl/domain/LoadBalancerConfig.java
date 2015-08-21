package com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain;

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;

public class LoadBalancerConfig {

    private String name;
    private String description;
    private LoadBalancerStatus status = LoadBalancerStatus.ENABLED;
    private DataCenter dataCenter;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LoadBalancerConfig name(String name) {
        setName(name);
        return this;
    }

    public LoadBalancerStatus getStatus() {
        return status;
    }

    public void setStatus(LoadBalancerStatus status) {
        this.status = status;
    }

    public LoadBalancerConfig status(LoadBalancerStatus status) {
        setStatus(status);
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LoadBalancerConfig description(String description) {
        setDescription(description);
        return this;
    }

    public DataCenter getDataCenter() {
        return dataCenter;
    }

    public void setDataCenter(DataCenter dataCenter) {
        this.dataCenter = dataCenter;
    }

    public LoadBalancerConfig dataCenter(DataCenter dataCenter) {
        setDataCenter(dataCenter);
        return this;
    }
}

package com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain;

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.group.LoadBalancer;

public class LoadBalancerPoolConfig {

    private Integer port;
    private LoadBalancerPoolMethod method = LoadBalancerPoolMethod.ROUND_ROBIN;
    private LoadBalancerPoolPersistence persistence = LoadBalancerPoolPersistence.STANDARD;
    private LoadBalancer loadBalancer;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public LoadBalancerPoolConfig port(Integer port) {
        setPort(port);
        return this;
    }

    public LoadBalancerPoolMethod getMethod() {
        return method;
    }

    public void setMethod(LoadBalancerPoolMethod method) {
        this.method = method;
    }

    public LoadBalancerPoolConfig method(LoadBalancerPoolMethod method) {
        setMethod(method);
        return this;
    }

    public LoadBalancerPoolPersistence getPersistence() {
        return persistence;
    }

    public void setPersistence(LoadBalancerPoolPersistence persistence) {
        this.persistence = persistence;
    }

    public LoadBalancerPoolConfig persistence(LoadBalancerPoolPersistence persistence) {
        setPersistence(persistence);
        return this;
    }

    public LoadBalancer getLoadBalancer() {
        return loadBalancer;
    }

    public void setLoadBalancer(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    public LoadBalancerPoolConfig loadBalancer(LoadBalancer loadBalancer) {
        setLoadBalancer(loadBalancer);
        return this;
    }
}

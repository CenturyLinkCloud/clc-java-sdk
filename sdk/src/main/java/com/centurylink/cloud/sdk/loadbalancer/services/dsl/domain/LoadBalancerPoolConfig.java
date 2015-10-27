/*
 * (c) 2015 CenturyLink. All Rights Reserved.
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

package com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain;

import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.group.LoadBalancer;

import java.util.Arrays;
import java.util.List;

public class LoadBalancerPoolConfig {

    private String id;
    private Integer port;
    private LoadBalancerPoolMethod method = LoadBalancerPoolMethod.ROUND_ROBIN;
    private LoadBalancerPoolPersistence persistence = LoadBalancerPoolPersistence.STANDARD;
    private LoadBalancer loadBalancer;
    private List<LoadBalancerNodeMetadata> nodes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LoadBalancerPoolConfig id(String id) {
        setId(id);
        return this;
    }

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

    public List<LoadBalancerNodeMetadata> getNodes() {
        return nodes;
    }

    public LoadBalancerPoolConfig nodes(List<LoadBalancerNodeMetadata> nodes) {
        this.nodes = nodes;
        return this;
    }

    public LoadBalancerPoolConfig nodes(LoadBalancerNodeMetadata... nodes) {
        this.nodes = Arrays.asList(nodes);
        return this;
    }
}

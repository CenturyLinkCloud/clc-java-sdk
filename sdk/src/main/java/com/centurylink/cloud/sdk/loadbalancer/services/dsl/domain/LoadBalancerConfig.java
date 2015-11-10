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

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;

import java.util.Arrays;
import java.util.List;

public class LoadBalancerConfig {

    private String name;
    private String description;
    private LoadBalancerStatus status = LoadBalancerStatus.ENABLED;
    private DataCenter dataCenter;
    private List<LoadBalancerPoolConfig> pool;

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

    public List<LoadBalancerPoolConfig> getPool() {
        return pool;
    }

    public LoadBalancerConfig pool(List<LoadBalancerPoolConfig> pool) {
        this.pool = pool;
        return this;
    }

    public LoadBalancerConfig pool(LoadBalancerPoolConfig... pools) {
        this.pool = Arrays.asList(pools);
        return this;
    }
}

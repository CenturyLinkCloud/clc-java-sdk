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

package com.centurylink.cloud.sdk.loadbalancer.services.client.domain;

import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerStatus;

public class LoadBalancerRequest {

    private String name;
    private String description;
    private String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LoadBalancerRequest name(String name) {
        setName(name);
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LoadBalancerRequest description(String description) {
        setDescription(description);
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(LoadBalancerStatus status) {
        this.status = status.getCode();
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LoadBalancerRequest status(LoadBalancerStatus status) {
        setStatus(status);
        return this;
    }
}

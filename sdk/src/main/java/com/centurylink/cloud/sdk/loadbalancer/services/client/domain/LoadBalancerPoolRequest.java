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

import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerPoolMethod;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerPoolPersistence;

public class LoadBalancerPoolRequest {

    private Integer port;
    private String method;
    private String persistence;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public LoadBalancerPoolRequest port(Integer port) {
        setPort(port);
        return this;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setMethod(LoadBalancerPoolMethod method) {
        this.method = method.getCode();
    }

    public LoadBalancerPoolRequest method(LoadBalancerPoolMethod method) {
        setMethod(method);
        return this;
    }

    public String getPersistence() {
        return persistence;
    }

    public void setPersistence(String persistence) {
        this.persistence = persistence;
    }

    public void setPersistence(LoadBalancerPoolPersistence persistence) {
        this.persistence = persistence.getCode();
    }

    public LoadBalancerPoolRequest persistence(LoadBalancerPoolPersistence persistence) {
        setPersistence(persistence);
        return this;
    }
}

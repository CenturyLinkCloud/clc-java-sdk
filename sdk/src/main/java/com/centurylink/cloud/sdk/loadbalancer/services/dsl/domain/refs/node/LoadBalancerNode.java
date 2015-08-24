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

package com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.node;

import com.centurylink.cloud.sdk.core.services.refs.Reference;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerStatus;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.filter.LoadBalancerNodeFilter;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.pool.LoadBalancerPool;

public class LoadBalancerNode implements Reference<LoadBalancerNodeFilter> {

    private LoadBalancerStatus status = LoadBalancerStatus.ENABLED;
    private String ipAddress;
    private String port;

    private final LoadBalancerPool loadBalancerPool;

    LoadBalancerNode(LoadBalancerStatus status, String ipAddress, String port, LoadBalancerPool loadBalancerPool) {
        this.status = status;
        this.ipAddress = ipAddress;
        this.port = port;
        this.loadBalancerPool = loadBalancerPool;
    }

    public static LoadBalancerNode ref(
            LoadBalancerStatus status,
            String ipAddress,
            String port,
            LoadBalancerPool loadBalancerPool
    ) {
        return new LoadBalancerNode(status, ipAddress, port, loadBalancerPool);
    }


    public LoadBalancerStatus getStatus() {
        return status;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getPort() {
        return port;
    }

    public LoadBalancerPool getLoadBalancerPool() {
        return loadBalancerPool;
    }

    @Override
    public String toString() {
        return this.toReadableString();
    }

    @Override
    public LoadBalancerNodeFilter asFilter() {
        return new LoadBalancerNodeFilter().loadBalancerPools(getLoadBalancerPool());
    }
}

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

package com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.group;

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.services.refs.Reference;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.filter.LoadBalancerFilter;

/**
 * {@inheritDoc}
 */
public abstract class LoadBalancer implements Reference<LoadBalancerFilter> {
    private final DataCenter dataCenter;

    LoadBalancer(DataCenter dataCenter) {
        this.dataCenter = dataCenter;
    }

    public static LoadBalancerByNameRef refByName(String name, DataCenter dataCenter) {
        return new LoadBalancerByNameRef(name, dataCenter);
    }

    public static LoadBalancerByIdRef refById(String id, DataCenter dataCenter) {
        return new LoadBalancerByIdRef(id, dataCenter);
    }

    public DataCenter getDataCenter() {
        return dataCenter;
    }

    @Override
    public String toString() {
        return this.toReadableString();
    }
}

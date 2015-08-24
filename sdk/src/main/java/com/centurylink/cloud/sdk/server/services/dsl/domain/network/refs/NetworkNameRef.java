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

package com.centurylink.cloud.sdk.server.services.dsl.domain.network.refs;

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.network.filters.NetworkFilter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Reference that allow to find network by owned data center and network name
 *
 * @author Aliaksandr Krasitski
 */
public class NetworkNameRef extends Network {
    private final DataCenter dataCenter;
    private final String name;

    NetworkNameRef(DataCenter dataCenter, String name) {
        this.dataCenter = dataCenter;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public DataCenter getDataCenter() {
        return dataCenter;
    }

    public NetworkNameRef name(String name) {
        return new NetworkNameRef(dataCenter, name);
    }

    public NetworkNameRef dataCenter(DataCenter dataCenter) {
        return new NetworkNameRef(dataCenter, name);
    }

    @Override
    public NetworkFilter asFilter() {
        checkNotNull(name, "Name must be not null");

        return new NetworkFilter()
            .dataCenters(dataCenter)
            .names(name);
    }
}

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
import com.centurylink.cloud.sdk.core.services.refs.Reference;
import com.centurylink.cloud.sdk.server.services.dsl.domain.network.filters.NetworkFilter;

/**
 * {@inheritDoc}
 */
public abstract class Network implements Reference<NetworkFilter> {

    /**
     * Method allow to refer network by it's ID. Comparison is by full match and case sensitive.
     *
     * @param id is not null ID of network
     * @return {@link NetworkByIdRef}
     */
    public static NetworkByIdRef refById(String id) {
        return new NetworkByIdRef(id);
    }

    /**
     * Method allow to refer network by name. Filtering is by full match. Comparison is case insensitive.
     *
     * @return {@link NetworkNameRef}
     */
    public static NetworkNameRef refByName() {
        return new NetworkNameRef(null, null);
    }

    public static NetworkNameRef refByName(DataCenter dataCenter, String name) {
        return new NetworkNameRef(dataCenter, name);
    }

    public static NetworkByGatewayRef refByGateway() {
        return new NetworkByGatewayRef(null, null);
    }

    public static NetworkByGatewayRef refByGateway(DataCenter dataCenter, String gateway) {
        return new NetworkByGatewayRef(dataCenter, gateway);
    }

    @Override
    public String toString() {
        return this.toReadableString();
    }
}

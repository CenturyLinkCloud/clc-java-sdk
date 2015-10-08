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

import com.centurylink.cloud.sdk.server.services.dsl.domain.network.filters.NetworkFilter;

import static com.centurylink.cloud.sdk.core.preconditions.Preconditions.checkNotNull;

/**
 * Reference that allow to find unique network by it's ID and owned data center reference
 *
 * @author Aliaksandr Krasitski
 */
public class NetworkByIdRef extends Network {
    private final String id;

    NetworkByIdRef(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public NetworkFilter asFilter() {
        checkNotNull(id, "Network ID must be not null");

        return new NetworkFilter().id(id);
    }
}

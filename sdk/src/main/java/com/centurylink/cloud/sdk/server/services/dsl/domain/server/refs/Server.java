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

package com.centurylink.cloud.sdk.server.services.dsl.domain.server.refs;

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.services.refs.Reference;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.filters.ServerFilter;

import static com.centurylink.cloud.sdk.core.preconditions.Preconditions.checkNotNull;


/**
 * {@inheritDoc}
 */
public abstract class Server implements Reference<ServerFilter> {

    /**
     * Method allow to refer server by it's ID. Matching is case insensitive.
     * Comparison by full match.
     *
     * @param id is ID of needed server
     * @return {@link ServerByIdRef}
     */
    public static ServerByIdRef refById(String id) {
        return new ServerByIdRef(id);
    }

    public static ServerByDescriptionRef refByDescription(DataCenter dataCenter, String keyword) {
        checkNotNull(dataCenter, "Datacenter must be not null");
        checkNotNull(keyword, "Keyword must be not null");

        return new ServerByDescriptionRef(dataCenter, keyword);
    }

    public static ServerByDescriptionRef refByDescription(String keyword) {
        checkNotNull(keyword, "Keyword must be not null");

        return new ServerByDescriptionRef(null, keyword);
    }

    public static ServerByDescriptionRef refByDescription() {
        return new ServerByDescriptionRef(null, null);
    }

    @Override
    public String toString() {
        return this.toReadableString();
    }
}

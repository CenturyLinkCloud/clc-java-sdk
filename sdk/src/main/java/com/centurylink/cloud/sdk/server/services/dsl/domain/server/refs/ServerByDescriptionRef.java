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
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.filters.ServerFilter;

import static com.centurylink.cloud.sdk.core.preconditions.ArgumentPreconditions.notNull;

/**
 * @author Ilya Drabenia
 */
public class ServerByDescriptionRef extends Server {
    private final DataCenter dataCenter;
    private final String keyword;

    public ServerByDescriptionRef(DataCenter dataCenter, String keyword) {
        this.dataCenter = dataCenter;
        this.keyword = keyword;
    }

    @Override
    public ServerFilter asFilter() {
        return
            new ServerFilter() {{
                if (dataCenter != null) {
                    dataCenters(dataCenter);
                }

                descriptionContains(keyword);
            }};
    }

    public DataCenter getDataCenter() {
        return dataCenter;
    }

    public String getKeyword() {
        return keyword;
    }

    public ServerByDescriptionRef dataCenter(DataCenter dataCenter) {
        notNull(dataCenter, "Data center must be not null");

        return new ServerByDescriptionRef(dataCenter, this.keyword);
    }

    public ServerByDescriptionRef description(String keyword) {
        notNull(keyword, "Keyword must be not null");

        return new ServerByDescriptionRef(this.dataCenter, keyword);
    }
}

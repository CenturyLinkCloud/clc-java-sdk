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

package com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs;

import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.filters.DataCenterFilter;

/**
 * Class allow to specify data center by name.
 * Name matching will be case insensitive and will use substring search.
 *
 * @author ilya.drabenia
 */
public class DataCenterByNameRef extends DataCenter {
    private final String name;

    DataCenterByNameRef(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public DataCenterFilter asFilter() {
        return new DataCenterFilter().nameContains(name);
    }
}

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

package com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs;

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.core.services.refs.Reference;

/**
 * {@inheritDoc}
 */
public abstract class DataCenter implements Reference<DataCenterFilter> {
    public static final DataCenterByIdRef CA_VANCOUVER = refById("ca1");
    public static final DataCenterByIdRef CA_TORONTO_1 = refById("ca2");
    public static final DataCenterByIdRef CA_TORONTO_2 = refById("ca3");
    public static final DataCenterByIdRef DE_FRANKFURT = refById("de1");
    public static final DataCenterByIdRef GB_PORTSMOUTH = refById("gb1");
    public static final DataCenterByIdRef GB_SLOUGH = refById("gb3");
    public static final DataCenterByIdRef US_CENTRAL_CHICAGO = refById("il1");
    public static final DataCenterByIdRef SG_APAC = refById("sg1");
    public static final DataCenterByIdRef US_CENTRAL_SALT_LAKE_CITY = refById("ut1");
    public static final DataCenterByIdRef US_EAST_NEW_YORK = refById("ny1");
    public static final DataCenterByIdRef US_EAST_STERLING = refById("va1");
    public static final DataCenterByIdRef US_WEST_SANTA_CLARA = refById("uc1");
    public static final DataCenterByIdRef US_WEST_SEATTLE = refById("wa1");

    /**
     * Method allow to refer datacenter by it's ID. Filtering is by full match.
     * Comparison is case insensitive.
     *
     * @param id is not null reference to datacenter
     * @return {@link DataCenterByIdRef}
     */
    public static DataCenterByIdRef refById(String id) {
        return new DataCenterByIdRef(id);
    }

    /**
     * Method allow to refer datacenter by keyword in it's name.
     * Matching use substring search. Comparison is case insensitive.
     *
     * @param name is not null keyword contains in target datacenter name
     * @return {@link DataCenterByNameRef}
     */
    public static DataCenterByNameRef refByName(String name) {
        return new DataCenterByNameRef(name);
    }

    @Override
    public String toString() {
        return this.toReadableString();
    }
}

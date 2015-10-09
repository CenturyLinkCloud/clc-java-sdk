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

package com.centurylink.cloud.sdk.policy.services.dsl.domain.refs;

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.filters.AntiAffinityPolicyFilter;

import static com.centurylink.cloud.sdk.core.preconditions.Preconditions.checkNotNull;

/**
 * Reference that allow to find policy by owned data center and policy name
 *
 * @author Aliaksandr Krasitski
 */
public class AntiAffinityPolicyNameRef extends AntiAffinityPolicy {
    private final DataCenter dataCenter;
    private final String name;

    AntiAffinityPolicyNameRef(DataCenter dataCenter, String name) {
        this.dataCenter = dataCenter;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public DataCenter getDataCenter() {
        return dataCenter;
    }

    public AntiAffinityPolicyNameRef name(String name) {
        return new AntiAffinityPolicyNameRef(dataCenter, name);
    }

    public AntiAffinityPolicyNameRef dataCenter(DataCenter dataCenter) {
        return new AntiAffinityPolicyNameRef(dataCenter, name);
    }

    @Override
    public AntiAffinityPolicyFilter asFilter() {
        checkNotNull(name, "Name must be not null");

        return new AntiAffinityPolicyFilter()
            .dataCenters(dataCenter)
            .names(name);
    }
}

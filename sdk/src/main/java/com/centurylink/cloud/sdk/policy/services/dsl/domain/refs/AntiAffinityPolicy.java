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
import com.centurylink.cloud.sdk.core.services.refs.Reference;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.filters.AntiAffinityPolicyFilter;

/**
 * {@inheritDoc}
 */
public abstract class AntiAffinityPolicy implements Reference<AntiAffinityPolicyFilter> {

    /**
     * Method allow to refer policy by it's ID. Comparison is by full match and case sensitive.
     *
     * @param id is not null ID of policy
     * @return {@link AntiAffinityPolicyByIdRef}
     */
    public static AntiAffinityPolicyByIdRef refById(String id) {
        return new AntiAffinityPolicyByIdRef(id);
    }

    /**
     * Method allow to refer policy by name. Filtering is by full match. Comparison is case insensitive.
     *
     * @return {@link AntiAffinityPolicyNameRef}
     */
    public static AntiAffinityPolicyNameRef refByName() {
        return new AntiAffinityPolicyNameRef(null, null);
    }

    public static AntiAffinityPolicyNameRef refByName(DataCenter dataCenter, String name) {
        return new AntiAffinityPolicyNameRef(dataCenter, name);
    }

    @Override
    public String toString() {
        return this.toReadableString();
    }
}

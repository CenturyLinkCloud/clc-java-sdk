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

import com.centurylink.cloud.sdk.policy.services.dsl.domain.filters.AlertPolicyFilter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Reference that allow to find policy by policy name
 *
 * @author Aliaksandr Krasitski
 */
public class AlertPolicyNameRef extends AlertPolicy {
    private final String name;

    AlertPolicyNameRef(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public AlertPolicyNameRef name(String name) {
        return new AlertPolicyNameRef(name);
    }

    @Override
    public AlertPolicyFilter asFilter() {
        checkNotNull(name, "Name must be not null");

        return new AlertPolicyFilter()
            .names(name);
    }
}

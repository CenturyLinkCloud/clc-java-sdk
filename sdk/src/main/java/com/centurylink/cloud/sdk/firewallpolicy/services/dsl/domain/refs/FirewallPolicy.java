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

package com.centurylink.cloud.sdk.firewallpolicy.services.dsl.domain.refs;

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.services.refs.Reference;
import com.centurylink.cloud.sdk.firewallpolicy.services.dsl.domain.filter.FirewallPolicyFilter;

/**
 * {@inheritDoc}
 */
public abstract class FirewallPolicy implements Reference<FirewallPolicyFilter> {
    private final DataCenter dataCenter;

    FirewallPolicy(DataCenter dataCenter) {
        this.dataCenter = dataCenter;
    }

    public static FirewallPolicyByIdRef refById(String id, DataCenter dataCenter) {
        return new FirewallPolicyByIdRef(id, dataCenter);
    }

    public DataCenter getDataCenter() {
        return dataCenter;
    }

    @Override
    public String toString() {
        return this.toReadableString();
    }
}

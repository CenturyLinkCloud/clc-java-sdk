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

package com.centurylink.cloud.sdk.server.services.dsl.domain;

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.AlertPolicyConfig;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.AntiAffinityPolicyConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.GroupHierarchyConfig;

import java.util.ArrayList;
import java.util.List;

import static com.centurylink.cloud.sdk.core.preconditions.Preconditions.checkNotNull;
import static java.util.Arrays.asList;

/**
 * Hierarchy configuration with groups and servers for provided data centers.
 * Should contain data center {@code dataCenter}
 * and {@link GroupHierarchyConfig}
 * {@code subitems}.
 * 
 * @author Aliaksandr Krasitski
 */
public class InfrastructureConfig {
    private List<GroupHierarchyConfig> subitems = new ArrayList<>();
    private List<DataCenter> dataCenters = new ArrayList<>();
    private List<AntiAffinityPolicyConfig> antiAffinityPolicies = new ArrayList<>();
    private List<AlertPolicyConfig> alertPolicies = new ArrayList<>();

    public static InfrastructureConfig dataCenter(DataCenter... dataCenters) {
        return
            new InfrastructureConfig()
                .dataCenters(dataCenters);
    }

    /**
     * Returns list of hierarchy
     * @return list of sub items
     */
    public List<GroupHierarchyConfig> getSubitems() {
        return subitems;
    }

    /**
     *
     * @param subitems array of {@link GroupHierarchyConfig}
     * @return current class instance
     */
    public InfrastructureConfig subitems(GroupHierarchyConfig... subitems) {
        checkNotNull(subitems, "List of server configs must be not a null");
        this.subitems.addAll(asList(subitems));
        return this;
    }

    /**
     * Returns data centers
     * @return list of data centers
     */
    public List<DataCenter> getDataCenters() {
        return dataCenters;
    }

    /**
     *
     * @param datacenters array of data centers
     * @return current class instance
     */
    public InfrastructureConfig dataCenters(DataCenter... datacenters) {
        checkNotNull(datacenters, "List of dataCenters must be not a null");
        this.dataCenters.addAll(asList(datacenters));
        return this;
    }

    /**
     * Returns anti-affinity policies configs
     * @return list of anti-affinity policies configs
     */
    public List<AntiAffinityPolicyConfig> getAntiAffinityPolicies() {
        return antiAffinityPolicies;
    }

    /**
     *
     * @param antiAffinityPolicies array of anti-affinity policies configs
     * @return current class instance
     */
    public InfrastructureConfig antiAffinityPolicies(AntiAffinityPolicyConfig... antiAffinityPolicies) {
        this.antiAffinityPolicies.addAll(asList(antiAffinityPolicies));
        return this;
    }

    /**
     * Returns alert policies configs
     * @return list of alert policies
     */
    public List<AlertPolicyConfig> getAlertPolicies() {
        return alertPolicies;
    }

    /**
     *
     * @param alertPolicies array of alert policies configs
     * @return current class instance
     */
    public InfrastructureConfig alertPolicies(AlertPolicyConfig... alertPolicies) {
        this.alertPolicies.addAll(asList(alertPolicies));
        return this;
    }
}

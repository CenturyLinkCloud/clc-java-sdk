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

package com.centurylink.cloud.sdk.policy.services.dsl.domain.firewall;

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;

import java.util.ArrayList;
import java.util.List;

public class FirewallPolicyConfig {

    private boolean enabled = true;
    private String destinationAccount;
    private List<String> source = new ArrayList<>();
    private List<String> destination = new ArrayList<>();
    private List<String> ports = new ArrayList<>();
    private DataCenter dataCenter;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public FirewallPolicyConfig enabled(boolean enabled) {
        setEnabled(enabled);
        return this;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public FirewallPolicyConfig destinationAccount(String destinationAccount) {
        setDestinationAccount(destinationAccount);
        return this;
    }

    public List<String> getSource() {
        return source;
    }

    public void setSource(List<String> source) {
        this.source = source;
    }

    public FirewallPolicyConfig source(List<String> source) {
        setSource(source);
        return this;
    }

    public List<String> getDestination() {
        return destination;
    }

    public void setDestination(List<String> destination) {
        this.destination = destination;
    }

    public FirewallPolicyConfig destination(List<String> destination) {
        setDestination(destination);
        return this;
    }

    public List<String> getPorts() {
        return ports;
    }

    public void setPorts(List<String> ports) {
        this.ports = ports;
    }

    public FirewallPolicyConfig ports(List<String> ports) {
        setPorts(ports);
        return this;
    }

    public DataCenter getDataCenter() {
        return dataCenter;
    }

    public void setDataCenter(DataCenter dataCenter) {
        this.dataCenter = dataCenter;
    }

    public FirewallPolicyConfig dataCenter(DataCenter dataCenter) {
        setDataCenter(dataCenter);
        return this;
    }
}

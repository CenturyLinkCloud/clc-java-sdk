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

package com.centurylink.cloud.sdk.servers.services.domain.server;

import com.centurylink.cloud.sdk.networks.services.domain.refs.NetworkRef;
import com.centurylink.cloud.sdk.servers.services.domain.ip.CreatePublicIpConfig;

/**
 * @author ilya.drabenia
 */
public class NetworkConfig {
    private NetworkRef network;
    private String primaryDns;
    private String secondaryDns;
    private CreatePublicIpConfig publicIpConfig;

    public String getPrimaryDns() {
        return primaryDns;
    }

    public void setPrimaryDns(String primaryDns) {
        this.primaryDns = primaryDns;
    }

    public NetworkConfig primaryDns(String primaryDns) {
        setPrimaryDns(primaryDns);
        return this;
    }

    public String getSecondaryDns() {
        return secondaryDns;
    }

    public void setSecondaryDns(String secondaryDns) {
        this.secondaryDns = secondaryDns;
    }

    public NetworkConfig secondaryDns(String secondaryDns) {
        setSecondaryDns(secondaryDns);
        return this;
    }

    public NetworkRef getNetwork() {
        return network;
    }

    public void setNetwork(NetworkRef network) {
        this.network = network;
    }

    public NetworkConfig network(NetworkRef network) {
        setNetwork(network);
        return this;
    }

    public CreatePublicIpConfig getPublicIpConfig() {
        return publicIpConfig;
    }

    public void setPublicIpConfig(CreatePublicIpConfig publicIpConfig) {
        this.publicIpConfig = publicIpConfig;
    }

    public NetworkConfig publicIpConfig(CreatePublicIpConfig publicIpConfig) {
        setPublicIpConfig(publicIpConfig);
        return this;
    }
}

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

package com.centurylink.cloud.sdk.server.services.dsl.domain.network;

import com.centurylink.cloud.sdk.server.services.dsl.domain.network.refs.Network;

/**
 * @author Aliaksandr Krasitski
 */
public class AddNetworkConfig {
    private Network network;
    private String ipAddress;

    public Network getNetwork() {
        return network;
    }

    public AddNetworkConfig network(Network network) {
        this.network = network;
        return this;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public AddNetworkConfig ipAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }
}

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

package com.centurylink.cloud.sdk.server.services.dsl.domain.ip;

import com.centurylink.cloud.sdk.server.services.client.domain.ip.PortConfig;
import com.centurylink.cloud.sdk.server.services.client.domain.ip.PublicIpRequest;
import com.centurylink.cloud.sdk.server.services.dsl.domain.ip.port.PortRangeConfig;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author Aliaksandr Krasitski
 */
public class PublicIpConverter {
    public PublicIpRequest createPublicIpRequest(CreatePublicIpConfig publicIpConfig) {
        return new PublicIpRequest()
            .internalIPAddress(publicIpConfig.getInternalIpAddress())
            .sourceRestrictions(publicIpConfig.getRestrictions().stream()
                .map(Subnet::getCidr)
                .collect(toList()))
            .ports(convertPorts(publicIpConfig.getPorts()));
    }

    public PublicIpRequest createPublicIpRequest(ModifyPublicIpConfig publicIpConfig) {
        return new PublicIpRequest()
            .sourceRestrictions(publicIpConfig.getRestrictions().stream()
                .map(Subnet::getCidr)
                .collect(toList()))
            .ports(convertPorts(publicIpConfig.getPorts()));
    }

    private List<PortConfig> convertPorts(
        List<com.centurylink.cloud.sdk.server.services.dsl.domain.ip.port.PortConfig> portConfigs) {

        return portConfigs.stream()
            .map(port -> new PortConfig()
                .protocol(port.getProtocolType().name())
                .port(port.getPort())
                .portTo(port instanceof PortRangeConfig ? ((PortRangeConfig) port).getPortTo() : null))
            .collect(toList());
    }
}

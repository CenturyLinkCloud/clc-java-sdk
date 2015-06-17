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


import com.centurylink.cloud.sdk.core.function.Streams;
import com.centurylink.cloud.sdk.server.services.dsl.domain.ip.port.PortConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.ip.port.SinglePortConfig;

import java.util.ArrayList;
import java.util.List;

import static com.centurylink.cloud.sdk.core.function.Streams.map;
import static java.util.Arrays.asList;

/**
 * @author Ilya Drabenia
 */
public class ModifyPublicIpConfig {
    private List<PortConfig> ports = new ArrayList<>();
    private List<Subnet> restrictions = new ArrayList<>();

    public List<PortConfig> getPorts() {
        return ports;
    }

    public ModifyPublicIpConfig openPorts(Integer... ports) {
        this.ports.addAll(
            Streams.map(ports, SinglePortConfig::new)
        );

        return this;
    }

    public ModifyPublicIpConfig openPorts(PortConfig... ports) {
        this.ports.addAll(asList(ports));

        return this;
    }

    public List<Subnet> getRestrictions() {
        return restrictions;
    }

    public ModifyPublicIpConfig sourceRestrictions(String... restrictions) {
        this.restrictions.addAll(
            map(restrictions, Subnet::new)
        );

        return this;
    }

    public ModifyPublicIpConfig sourceRestrictions(Subnet... restrictions) {
        this.restrictions.addAll(
            asList(restrictions)
        );

        return this;
    }
}

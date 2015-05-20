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

package com.centurylink.cloud.sdk.servers.services.domain.ip.port;

import com.centurylink.cloud.sdk.servers.services.domain.ip.ProtocolType;
import com.google.common.base.Preconditions;

/**
 * @author Ilya Drabenia
 */
public class PortConfig {
    public static final Integer HTTP = 80;
    public static final Integer HTTPS = 443;
    public static final Integer SSH = 22;
    public static final Integer RDP = 3389; //
    public static final Integer FTP = 21; //

    private ProtocolType protocolType = ProtocolType.TCP;
    protected Integer port;

    private static final int MIN_PORT = 0;
    private static final int MAX_PORT = 65535;

    public PortRangeConfig to(Integer to) {
        Preconditions.checkArgument(to > MIN_PORT && to < MAX_PORT && this.port < to);
        return new PortRangeConfig(this.protocolType, this.port, to);
    }

    public Integer getPort() {
        return port;
    }

    public SinglePortConfig port(Integer port) {
        Preconditions.checkArgument(port > MIN_PORT && port < MAX_PORT);

        return new SinglePortConfig(protocolType, port);
    }

    public ProtocolType getProtocolType() {
        return protocolType;
    }

    // default is TCP
    public PortConfig protocol(ProtocolType protocolType) {
        this.protocolType = protocolType;
        return this;
    }

}

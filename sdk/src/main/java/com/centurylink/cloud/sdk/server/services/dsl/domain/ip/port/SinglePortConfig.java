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

package com.centurylink.cloud.sdk.server.services.dsl.domain.ip.port;

import com.centurylink.cloud.sdk.server.services.dsl.domain.ip.ProtocolType;

/**
 * @author Ilya Drabenia
 */
public class SinglePortConfig extends PortConfig {

    public SinglePortConfig(Integer port) {
        this(ProtocolType.TCP, port);
    }

    public SinglePortConfig(ProtocolType protocolType, Integer port) {
        super.protocol(protocolType);
        this.port = port;
    }
}

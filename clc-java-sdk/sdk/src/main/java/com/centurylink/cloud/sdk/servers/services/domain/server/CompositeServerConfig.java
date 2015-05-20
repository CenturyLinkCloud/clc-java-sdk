/*
 * (c) 2015 CenturyLink Cloud. All Rights Reserved.
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

/**
 * Represents multiple identical server configs.<br/>
 * Store {@code count} number of copies of {@code server}
 *
 * @author Aliaksandr Krasitski
 */
public class CompositeServerConfig implements ServerConfig {
    private int count = 1;
    private CreateServerConfig server;

    public CompositeServerConfig count(int count) {
        assert count > 0;
        this.count = count;
        return this;
    }

    public int getCount() {
        return count;
    }

    public CompositeServerConfig server(CreateServerConfig server) {
        this.server = server;
        return this;
    }

    public CreateServerConfig getServer() {
        return server;
    }

    @Override
    public CreateServerConfig[] getServerConfig() {
        CreateServerConfig[] copies = new CreateServerConfig[count];
        for (int i=0; i<count;i++) {
            copies[i] = (server);
        }
        return copies;
    }
}

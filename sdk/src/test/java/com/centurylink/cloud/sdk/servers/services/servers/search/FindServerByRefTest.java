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

package com.centurylink.cloud.sdk.servers.services.servers.search;

import com.centurylink.cloud.sdk.core.auth.AuthModule;
import com.centurylink.cloud.sdk.core.config.SdkConfiguration;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.ServersModule;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Ilya Drabenia
 */
public class FindServerByRefTest extends AbstractServersSdkTest {

    @Inject
    ServerService serverService;

    @Inject
    ServerClient serverClient;

    @Override
    protected List<Module> modules() {
        return list(new AuthModule(), Modules.override(new ServersModule()).with(new AbstractModule() {
            @Override
            protected void configure() {
                bind(ServerClient.class)
                    .toInstance(mock(ServerClient.class));
            }
        }));
    }
    
    @Test
    public void testFindServerByRef() {
        defineServer(new ServerMetadata()
            .id("de1altrserv1")
            .name("serv1")
        );

        ServerMetadata server = serverService.findByRef(Server.refById("de1altrserv1"));

        assert server.getName().equals("serv1");
    }

    private void defineServer(ServerMetadata server) {
        when(serverClient.findServerById(server.getId()))
            .thenReturn(server);
    }

}

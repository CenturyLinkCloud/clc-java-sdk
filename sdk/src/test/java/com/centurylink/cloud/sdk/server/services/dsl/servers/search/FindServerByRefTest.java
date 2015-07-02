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

package com.centurylink.cloud.sdk.server.services.dsl.servers.search;

import com.centurylink.cloud.sdk.core.auth.AuthModule;
import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.refs.Server;
import com.centurylink.cloud.sdk.server.services.ServerModule;
import com.centurylink.cloud.sdk.server.services.client.ServerClient;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
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
        return list(new AuthModule(), Modules.override(new ServerModule()).with(new AbstractModule() {
            @Override
            protected void configure() {
                bind(ServerClient.class)
                    .toInstance(mock(ServerClient.class));
            }
        }));
    }
    
    @Test
    public void testFindServerByRef() {
        String serverId = "de1altrserv1";
        String serverName = "serv1";

        defineServer(
            new ServerMetadata()
                .id(serverId)
                .name(serverName)
        );

        ServerMetadata server = serverService.findByRef(Server.refById(serverId));

        assertEquals(server.getName(), serverName);
    }

    private void defineServer(ServerMetadata server) {
        when(serverClient.findServerById(server.getId()))
            .thenReturn(server);
    }

}

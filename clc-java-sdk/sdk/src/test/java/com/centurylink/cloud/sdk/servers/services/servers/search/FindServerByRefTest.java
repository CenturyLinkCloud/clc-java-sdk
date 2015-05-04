package com.centurylink.cloud.sdk.servers.services.servers.search;

import com.centurylink.cloud.sdk.core.auth.AuthModule;
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

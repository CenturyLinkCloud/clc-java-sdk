package com.centurylink.cloud.sdk.servers;

import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.ServerRef;
import com.centurylink.cloud.sdk.tests.AbstractSdkTest;
import com.centurylink.cloud.sdk.core.auth.AuthModule;
import com.google.inject.Module;

import java.util.List;

import static com.centurylink.cloud.sdk.servers.services.TestServerSupport.anyServerConfig;

/**
 * @author ilya.drabenia
 */
public class AbstractServersSdkTest extends AbstractSdkTest {

    @Override
    protected List<Module> modules() {
        return list(new AuthModule(), new ServersModule());
    }

    protected ServerMetadata createDefaultServerWithName(ServerService serverService, String name) throws Exception {
        return serverService.create(anyServerConfig().name(name)).waitUntilComplete().getResult();
    }

    protected void cleanUpCreatedResources(ServerService serverService, ServerRef server) {
        serverService.delete(server).waitUntilComplete();
    }

}

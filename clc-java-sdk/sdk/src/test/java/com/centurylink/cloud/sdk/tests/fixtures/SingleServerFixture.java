package com.centurylink.cloud.sdk.tests.fixtures;

import com.centurylink.cloud.sdk.ClcSdk;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.server.*;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;
import com.centurylink.cloud.sdk.servers.services.domain.template.refs.Template;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.time.ZonedDateTime;

import static com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group.DEFAULT_GROUP;
import static com.centurylink.cloud.sdk.servers.services.domain.server.ServerType.STANDARD;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.CpuArchitecture.x86_64;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.OsType.CENTOS;
import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;
import static org.testng.Assert.assertEquals;

/**
 * @author Ilya Drabenia
 */
@Test(groups = {INTEGRATION, LONG_RUNNING})
public class SingleServerFixture {
    private static volatile SingleServerFixture instance;

    private final ServerService serverService = new ClcSdk().serverService();
    private volatile Server server;

    public static Server server() {
        return instance.getServer();
    }

    private Server getServer() {
        assertThatServerProperlyStarted(loadMetadataOf(server));

        return server;
    }

    @BeforeSuite(groups = LONG_RUNNING)
    public void createServer() {
        instance = this;

        server =
            serverService
                .create(new CreateServerConfig()
                    .name("TCRT")
                    .type(STANDARD)
                    .group(Group.refByName()
                        .name(DEFAULT_GROUP)
                        .dataCenter(DataCenter.refByName("FranKfUrt"))
                    )
                    .timeToLive(ZonedDateTime.now().plusDays(1))
                    .machine(new Machine()
                        .cpuCount(1)
                        .ram(3)
                        .disk(new DiskConfig()
                            .type(DiskType.RAW)
                            .size(14)
                        )
                    )
                    .template(Template.refByOs()
                        .dataCenter(DataCenter.DE_FRANKFURT)
                        .type(CENTOS)
                        .version("6")
                        .architecture(x86_64)
                    )
                    .network(new NetworkConfig()
                        .primaryDns("172.17.1.26")
                        .secondaryDns("172.17.1.27")
                    )
                )
                .waitUntilComplete()
                .getResult()
                .asRefById();

        assertThatServerProperlyStarted(
            loadMetadataOf(server)
        );
    }

    @AfterSuite(groups = LONG_RUNNING)
    public void deleteServer() {
        ServerMetadata serverStateBeforeDelete = loadMetadataOf(server);

        serverService.delete(server);

        assertThatServerProperlyStarted(serverStateBeforeDelete);
    }

    private ServerMetadata loadMetadataOf(Server curServer) {
        return serverService.findByRef(curServer);
    }

    @Test(enabled = false) // it's assert
    public void assertThatServerProperlyStarted(ServerMetadata metadata) {
        assertEquals(metadata.getLocationId().toUpperCase(), "DE1");
        assertEquals(metadata.getDetails().getPowerState(), "started");
        assertEquals(metadata.getStatus(), "active");
    }

}

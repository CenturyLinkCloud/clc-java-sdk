package com.centurylink.cloud.sdk.tests.fixtures;

import com.centurylink.cloud.sdk.ClcSdk;
import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.server.*;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.time.ZonedDateTime;

import static com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenter.DE_FRANKFURT;
import static com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group.DEFAULT_GROUP;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.CpuArchitecture.x86_64;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.OsType.CENTOS;
import static com.centurylink.cloud.sdk.servers.services.domain.server.ServerType.STANDARD;
import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;
import static org.testng.Assert.assertEquals;

/**
 * @author Ilya Drabenia
 */
@Test(groups = {INTEGRATION, LONG_RUNNING})
public class SingleServerFixture {
    private final ClcSdk clcSdk = new ClcSdk();
    private static volatile Server server;

    public static Server server() {
        return server;
    }

    @BeforeSuite(groups = LONG_RUNNING)
    public void createServer() {
        server =
            clcSdk
                .serverService()
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
                    .template(com.centurylink.cloud.sdk.servers.services.domain.template.refs.Template.refByOs()
                        .dataCenter(DE_FRANKFURT)
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

        assertThatServerProperlyStarted();
    }

    @Test(enabled = false) // it's assert, not a test
    public void assertThatServerProperlyStarted() {
        ServerMetadata metadata =
            clcSdk
                .serverService()
                .findByRef(server);

        assertEquals(metadata.getLocationId().toUpperCase(), "DE1");
        assertEquals(metadata.getDetails().getPowerState(), "started");
        assertEquals(metadata.getStatus(), "active");
    }

    @AfterSuite(groups = LONG_RUNNING)
    public void deleteServer() {
        assertThatServerProperlyStarted();

        clcSdk
            .serverService()
            .delete(server);
    }

}

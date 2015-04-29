package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.OperationFuture;
import com.centurylink.cloud.sdk.core.services.ResourceNotFoundException;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.ip.PublicIpConfig;
import com.centurylink.cloud.sdk.servers.services.domain.server.NetworkConfig;
import com.centurylink.cloud.sdk.servers.services.domain.server.TimeToLive;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.ServerByIdRef;
import com.centurylink.cloud.sdk.tests.fixtures.SingleServerFixture;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import java.time.ZonedDateTime;

import static com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenter.US_EAST_STERLING;
import static com.centurylink.cloud.sdk.servers.services.TestServerSupport.anyServerConfig;
import static com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group.DEFAULT_GROUP;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.CpuArchitecture.x86_64;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.OsType.RHEL;
import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * @author ilya.drabenia
 */
@Test(groups = {INTEGRATION, LONG_RUNNING})
public class ServerServiceTest extends AbstractServersSdkTest {

    @Inject
    ServerService serverService;

    @Test
    public void testCreate() throws Exception {
        Server serverRef = SingleServerFixture.server();

        ServerMetadata server = serverService.findByRef(serverRef);
        assert !isNullOrEmpty(server.getId());
        assertEquals(server.getLocationId().toUpperCase(), "DE1");
    }

    @Test(enabled = false) // This functionality tested by single server fixture
    public void testCreateWithTimeToLive() throws Exception {
        ZonedDateTime tomorrow = ZonedDateTime.now().plusDays(1);
        ServerMetadata newServer =
                serverService.create(anyServerConfig()
                    .name("CTTL")
                    .timeToLive(new TimeToLive(tomorrow))
                )
                .waitUntilComplete()
                .getResult();

        assert !isNullOrEmpty(newServer.getId());

        cleanUpCreatedResources(newServer.asRefById());
    }

    @Test
    public void testCreateServerWithPublicIp() throws Exception {
        ServerMetadata newServer =
            serverService.create(anyServerConfig()
                .name("CTTL")
                .network(new NetworkConfig()
                    .publicIp(new PublicIpConfig()
                        .openPorts(8080)
                    )
                )
            )
            .waitUntilComplete()
            .getResult();

        assert !isNullOrEmpty(newServer.getId());

        cleanUpCreatedResources(newServer.asRefById());
    }

    @Test
    public void testCreateWithManagedOS() {
        ServerMetadata newServer =
            serverService.create(anyServerConfig()
                .name("CMOS")
                .template(com.centurylink.cloud.sdk.servers.services.domain.template.refs.Template.refByOs()
                    .dataCenter(US_EAST_STERLING)
                    .type(RHEL)
                    .edition("6")
                    .architecture(x86_64)
                )
                .managedOs()
                .group(Group.refByName()
                    .name(DEFAULT_GROUP)
                    .dataCenter(US_EAST_STERLING)
                )
            )
            .waitUntilComplete()
            .getResult();

        assert !isNullOrEmpty(newServer.getId());

        cleanUpCreatedResources(newServer.asRefById());
    }

    @Test(enabled = false, expectedExceptions = ResourceNotFoundException.class)
    public void testDeleteServers() {
        OperationFuture<ServerMetadata> future1 = serverService.create(anyServerConfig());
        OperationFuture<ServerMetadata> future2 = serverService.create(anyServerConfig());

        ServerMetadata testServer1 = future1
                .waitUntilComplete()
                .getResult();

        ServerMetadata testServer2 = future2
                .waitUntilComplete()
                .getResult();

        Server ref1 = testServer1.asRefById();
        Server ref2 = testServer2.asRefById();

        serverService.delete(ref1.asFilter().id(ref2.as(ServerByIdRef.class).getId())).waitUntilComplete();



        //TODO find by non existing id throws com.fasterxml.jackson.databind.JsonMappingException
//        //catch only 1st call of finding server
//        try {
//            serverService.findByRef(ref1);
//        } catch (ResourceNotFoundException e) {
//            serverService.findByRef(ref2);
//        }

    }

    void cleanUpCreatedResources(Server newServer) {
        serverService
            .delete(newServer);
    }

}

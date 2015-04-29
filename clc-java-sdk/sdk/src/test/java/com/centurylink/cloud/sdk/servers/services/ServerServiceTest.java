package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.ClcSdk;
import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.DataCenter;
import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.DataCenters;
import com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.OperationFuture;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.group.Group;
import com.centurylink.cloud.sdk.servers.services.domain.ip.PublicIpConfig;
import com.centurylink.cloud.sdk.servers.services.domain.server.NetworkConfig;
import com.centurylink.cloud.sdk.servers.services.domain.server.TimeToLive;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.IdServerRef;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.ServerRef;
import com.centurylink.cloud.sdk.servers.services.domain.template.CreateTemplateCommand;
import com.centurylink.cloud.sdk.servers.services.domain.template.Template;
import com.centurylink.cloud.sdk.tests.fixtures.SingleServerFixture;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import java.time.ZonedDateTime;

import static com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.DataCenters.CA_VANCOUVER;
import static com.centurylink.cloud.sdk.servers.services.TestServerSupport.anyServerConfig;
import static com.centurylink.cloud.sdk.servers.services.domain.group.DefaultGroups.DEFAULT_GROUP;
import static com.centurylink.cloud.sdk.servers.services.domain.os.CpuArchitecture.x86_64;
import static com.centurylink.cloud.sdk.servers.services.domain.os.OsType.RHEL;
import static com.centurylink.cloud.sdk.servers.services.domain.template.CreateTemplateCommand.Visibility.PRIVATE;
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

    @Inject
    TemplateService templateService;

    @Test
    public void testCreate() throws Exception {
        ServerRef serverRef = SingleServerFixture.server();

        ServerMetadata server = serverService.findByRef(serverRef);
        assert !isNullOrEmpty(server.getId());
        assertEquals(server.getLocationId().toUpperCase(), "DE1");
    }

    @Test(enabled = false) // custom template endpoint is not documented yet
    public void testCreateWithCustomTemplate() throws Exception {
        Template customTemplate = createTemplateWithDescription("template1");

        ServerMetadata testServer = serverService
            .create(anyServerConfig()
                .template(Template.refByDescription()
                    .description("template1")
                )
            )
            .waitUntilComplete()
            .getResult();

        assert testServer.getId() != null;

        serverService.delete(testServer.asRefById());
        templateService.delete(customTemplate);
    }

    private Template createTemplateWithDescription(String description) {
        ServerMetadata templateServer = new TestServerSupport(serverService).createAnyServer();

        return serverService
            .convertToTemplate(new CreateTemplateCommand()
                .server(templateServer.asRefById())
                .visibility(PRIVATE)
                .password(TestServerSupport.PASSWORD)
                .description(description)
            )
            .waitUntilComplete()
            .getResult();
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
                .template(Template.refByOs()
                    .dataCenter(DataCenters.US_EAST_STERLING)
                    .type(RHEL)
                    .edition("6")
                    .architecture(x86_64)
                )
                .managedOs()
                .group(Group.refByName()
                    .name(DEFAULT_GROUP)
                    .dataCenter(DataCenters.US_EAST_STERLING)
                )
            )
            .waitUntilComplete()
            .getResult();

        assert !isNullOrEmpty(newServer.getId());

        cleanUpCreatedResources(newServer.asRefById());
    }

    //@Test(expectedExceptions = ResourceNotFoundException.class)
    public void testDeleteServers() {
        OperationFuture<ServerMetadata> future1 = serverService.create(anyServerConfig());
        OperationFuture<ServerMetadata> future2 = serverService.create(anyServerConfig());

        ServerMetadata testServer1 = future1
                .waitUntilComplete()
                .getResult();

        ServerMetadata testServer2 = future2
                .waitUntilComplete()
                .getResult();

        ServerRef ref1 = testServer1.asRefById();
        ServerRef ref2 = testServer2.asRefById();

        serverService.delete(ref1.asFilter().id(ref2.as(IdServerRef.class).getId())).waitUntilComplete();

        //TODO find by non existing id throws com.fasterxml.jackson.databind.JsonMappingException
//        //catch only 1st call of finding server
//        try {
//            serverService.findByRef(ref1);
//        } catch (ResourceNotFoundException e) {
//            serverService.findByRef(ref2);
//        }

    }

    void cleanUpCreatedResources(ServerRef newServer) {
        serverService
            .delete(newServer);
    }

    public static void main(String... args) {
        new ClcSdk()
            .serverService()
            .delete(new ServerFilter()
                .dataCenters(CA_VANCOUVER)
                .status("active", "archived")
            );
    }

}

package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.core.datacenters.services.domain.DataCenter;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.group.Group;
import com.centurylink.cloud.sdk.servers.services.domain.server.*;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.ServerRef;
import com.centurylink.cloud.sdk.servers.services.domain.template.CreateTemplateCommand;
import com.centurylink.cloud.sdk.servers.services.domain.template.Template;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import java.time.ZonedDateTime;

import static com.centurylink.cloud.sdk.servers.services.TestServerSupport.anyServerConfig;
import static com.centurylink.cloud.sdk.servers.services.domain.group.DefaultGroups.DEFAULT_GROUP;
import static com.centurylink.cloud.sdk.servers.services.domain.template.CreateTemplateCommand.Visibility.PRIVATE;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * @author ilya.drabenia
 */
@Test(groups = LONG_RUNNING)
public class ServerServiceTest extends AbstractServersSdkTest {

    @Inject
    ServerService serverService;

    @Inject
    TemplateService templateService;

    @Test
    public void testCreate() throws Exception {
        ServerMetadata newServer =
            serverService
                .create(anyServerConfig()
                    .name("TCRT")
                    .network(new NetworkConfig()
                        .primaryDns("172.17.1.26")
                        .secondaryDns("172.17.1.27")
                    )
                    .machine(new Machine()
                        .cpuCount(1)
                        .ram(3)
                        .disk(new DiskConfig()
                            .type(DiskType.RAW)
                            .size(14)
                        )
                    )
                )
                .waitUntilComplete()
                .getResult();

        assert !isNullOrEmpty(newServer.getId());

        cleanUpCreatedResources(newServer.asRefById());
    }

    @Test
    public void testCreateWithDataCenterLookup() throws Exception {
        ServerMetadata newServer =
            serverService.create(anyServerConfig()
                .name("CDCL")
                .group(Group.refByName()
                    .name(DEFAULT_GROUP)
                    .dataCenter(DataCenter.refByName("FranKfUrt"))
                )
            )
            .waitUntilComplete()
            .getResult();

        assert !isNullOrEmpty(newServer.getId());

        cleanUpCreatedResources(newServer.asRefById());
    }

    @Test(enabled = false)
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

    @Test
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

    void cleanUpCreatedResources(ServerRef newServer) {
        serverService
            .delete(newServer)
            .waitUntilComplete();
    }

}

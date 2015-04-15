package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.core.datacenters.services.domain.DataCenter;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.group.Group;
import com.centurylink.cloud.sdk.servers.services.domain.server.*;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.IdServerRef;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.ServerRef;
import com.centurylink.cloud.sdk.servers.services.domain.template.CreateTemplateCommand;
import com.centurylink.cloud.sdk.servers.services.domain.template.Template;
import com.centurylink.cloud.sdk.tests.fixtures.SingleServerFixture;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import java.time.ZonedDateTime;

import static com.centurylink.cloud.sdk.core.datacenters.services.domain.DataCenters.DE_FRANKFURT;
import static com.centurylink.cloud.sdk.core.datacenters.services.domain.DataCenters.US_CENTRAL_SALT_LAKE_CITY;
import static com.centurylink.cloud.sdk.servers.services.TestServerSupport.anyServerConfig;
import static com.centurylink.cloud.sdk.servers.services.domain.group.DefaultGroups.DEFAULT_GROUP;
import static com.centurylink.cloud.sdk.servers.services.domain.os.CpuArchitecture.x86_64;
import static com.centurylink.cloud.sdk.servers.services.domain.os.OsType.CENTOS;
import static com.centurylink.cloud.sdk.servers.services.domain.server.ServerType.STANDARD;
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

    void cleanUpCreatedResources(ServerRef newServer) {
        serverService
            .delete(newServer)
            .waitUntilComplete();
    }

}

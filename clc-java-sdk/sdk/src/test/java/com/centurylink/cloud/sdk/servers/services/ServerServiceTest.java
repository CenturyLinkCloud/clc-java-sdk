package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.server.Machine;
import com.centurylink.cloud.sdk.servers.services.domain.datacenter.DataCenter;
import com.centurylink.cloud.sdk.servers.services.domain.group.Group;
import com.centurylink.cloud.sdk.servers.services.domain.server.CreateServerCommand;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.ServerRef;
import com.centurylink.cloud.sdk.servers.services.domain.template.CreateTemplateCommand;
import com.centurylink.cloud.sdk.servers.services.domain.template.Template;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.servers.services.domain.datacenter.DataCenters.DE_FRANKFURT;
import static com.centurylink.cloud.sdk.servers.services.domain.datacenter.DataCenters.US_CENTRAL_SALT_LAKE_CITY;
import static com.centurylink.cloud.sdk.servers.services.domain.group.DefaultGroups.DEFAULT_GROUP;
import static com.centurylink.cloud.sdk.servers.services.domain.os.CpuArchitecture.x86_64;
import static com.centurylink.cloud.sdk.servers.services.domain.os.OsType.CENTOS;
import static com.centurylink.cloud.sdk.servers.services.domain.server.ServerType.STANDARD;
import static com.centurylink.cloud.sdk.servers.services.domain.template.CreateTemplateCommand.Visibility.PRIVATE;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * @author ilya.drabenia
 */
@Test(groups = "LongRunning")
public class ServerServiceTest extends AbstractServersSdkTest {

    @Inject
    ServerService serverService;

    @Inject
    TemplateService templateService;


    private CreateServerCommand anyServerConfig() {
        return new CreateServerCommand()
            .name("ALTRS1")
            .type(STANDARD)

            .group(Group.refByName()
                .name(DEFAULT_GROUP)
                .dataCenter(DE_FRANKFURT)
            )

            .machine(new Machine()
                .cpuCount(1)
                .ram(2)
            )

            .template(Template.refByOs()
                .dataCenter(US_CENTRAL_SALT_LAKE_CITY)
                .type(CENTOS)
                .version("6")
                .architecture(x86_64)
            );
    }

    @Test
    public void testCreate() throws Exception {
        ServerMetadata newServer =
            serverService
                .create(anyServerConfig())
                .waitUntilComplete()
                .getResult();

        assert !isNullOrEmpty(newServer.getId());

        cleanUpCreatedResources(newServer.asRefById());
    }

    @Test
    public void testCreateWithDataCenterLookup() throws Exception {
        ServerMetadata newServer =
            serverService.create(anyServerConfig()
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

    @Test
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

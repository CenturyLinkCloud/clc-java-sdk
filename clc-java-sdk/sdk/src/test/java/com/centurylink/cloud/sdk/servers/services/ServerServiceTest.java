package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.services.domain.datacenter.DataCenter;
import com.centurylink.cloud.sdk.servers.services.domain.datacenter.DataCenters;
import com.centurylink.cloud.sdk.servers.services.domain.group.DefaultGroups;
import com.centurylink.cloud.sdk.servers.services.domain.group.Group;
import com.centurylink.cloud.sdk.servers.services.domain.Machine;
import com.centurylink.cloud.sdk.servers.services.domain.server.CreateServerCommand;
import com.centurylink.cloud.sdk.servers.services.domain.template.CreateTemplateCommand;
import com.centurylink.cloud.sdk.servers.services.domain.template.Template;
import com.centurylink.cloud.sdk.servers.services.domain.os.OperatingSystem;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import javax.xml.crypto.Data;

import static com.centurylink.cloud.sdk.servers.services.domain.group.DefaultGroups.DEFAULT_GROUP;
import static com.centurylink.cloud.sdk.servers.services.domain.server.ServerType.STANDARD;
import static com.centurylink.cloud.sdk.servers.services.domain.datacenter.DataCenters.DE_FRANKFURT;
import static com.centurylink.cloud.sdk.servers.services.domain.os.CpuArchitecture.x86_64;
import static com.centurylink.cloud.sdk.servers.services.domain.os.OsType.CENTOS;
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
                .dataCenter(DataCenter.refById(DE_FRANKFURT.getId()))
            )

            .machine(new Machine()
                .cpuCount(1)
                .ram(2)
            )

            .template(new Template().os(new OperatingSystem()
                .type(CENTOS)
                .version("6")
                .architecture(x86_64)
            ));
    }

    @Test
    public void testCreate() throws Exception {
        CreateServerCommand newServer =
            serverService
                .create(anyServerConfig())
                .waitUntilComplete()
                .getResult();

        assert !isNullOrEmpty(newServer.getId());

        cleanUpCreatedResources(newServer);
    }

    @Test
    public void testCreateWithDataCenterLookup() throws Exception {
        CreateServerCommand newServer =
            serverService.create(anyServerConfig()
                .group(Group.refByName()
                    .name(DEFAULT_GROUP)
                    .dataCenter(DataCenter.refByName("FranKfUrt"))
                )
            )
            .waitUntilComplete()
            .getResult();

        assert !isNullOrEmpty(newServer.getId());

        cleanUpCreatedResources(newServer);
    }

    @Test
    public void testCreateWithCustomTemplate() throws Exception {
        Template customTemplate = createTemplateWithDescription("template1");

        CreateServerCommand testServer = serverService
            .create(anyServerConfig()
                .template(new Template().description("template1"))
            )
            .waitUntilComplete()
            .getResult();

        assert testServer.getId() != null;

        serverService.delete(testServer);
        templateService.delete(customTemplate);
    }

    private Template createTemplateWithDescription(String description) {
        CreateServerCommand templateServer = new TestServerSupport(serverService).createAnyServer();
        return serverService
            .convertToTemplate(new CreateTemplateCommand()
                .server(templateServer)
                .visibility(PRIVATE)
                .password(TestServerSupport.PASSWORD)
                .description(description)
            )
            .waitUntilComplete()
            .getResult();
    }

    void cleanUpCreatedResources(CreateServerCommand newServer) {
        serverService
            .delete(newServer)
            .waitUntilComplete();
    }

}

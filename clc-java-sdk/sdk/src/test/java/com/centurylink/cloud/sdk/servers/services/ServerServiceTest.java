package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.services.domain.datacenter.DataCenter;
import com.centurylink.cloud.sdk.servers.services.domain.group.Group;
import com.centurylink.cloud.sdk.servers.services.domain.Machine;
import com.centurylink.cloud.sdk.servers.services.domain.Server;
import com.centurylink.cloud.sdk.servers.services.domain.template.CreateTemplateCommand;
import com.centurylink.cloud.sdk.servers.services.domain.template.Template;
import com.centurylink.cloud.sdk.servers.services.domain.os.OperatingSystem;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.servers.services.domain.ServerType.STANDARD;
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


    private Server anyServerConfig() {
        return new Server()
            .name("ALTRS1")
            .type(STANDARD)

            .group(new Group()
                .dataCenter(DE_FRANKFURT)
                .name("Default Group")
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
        Server newServer =
            serverService
                .create(anyServerConfig())
                .waitUntilComplete()
                .getResult();

        assert !isNullOrEmpty(newServer.getId());

        cleanUpCreatedResources(newServer);
    }

    @Test
    public void testCreateWithDataCenterLookup() throws Exception {
        Server newServer =
            serverService.create(anyServerConfig()
                .group(new Group()
                    .dataCenter(new DataCenter().name("FranKfUrt"))
                    .name("Group3")
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

        Server testServer = serverService
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
        Server templateServer = new TestServerSupport(serverService).createAnyServer();
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

    void cleanUpCreatedResources(Server newServer) {
        serverService
            .delete(newServer)
            .waitUntilComplete();
    }

}

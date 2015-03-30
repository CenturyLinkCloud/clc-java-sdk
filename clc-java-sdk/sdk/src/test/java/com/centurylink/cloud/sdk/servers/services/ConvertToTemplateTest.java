package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.server.CreateServerCommand;
import com.centurylink.cloud.sdk.servers.services.domain.template.CreateTemplateCommand;
import com.centurylink.cloud.sdk.servers.services.domain.template.Template;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.servers.services.domain.template.CreateTemplateCommand.Visibility.PRIVATE;

/**
 * @author ilya.drabenia
 */
@Test(groups = {"LongRunning", "Integration"})
public class ConvertToTemplateTest extends AbstractServersSdkTest {

    @Inject
    private ServerService serverService;

    @Test
    public void testConvertToTemplate() {
        ServerMetadata server = new TestServerSupport(serverService).createAnyServer();

        Template template =
            serverService
                .convertToTemplate(new CreateTemplateCommand()
                    .server(server.asRefById())
                    .description("testTemplate")
                    .password("1qa@WS3ed")
                    .visibility(PRIVATE)
                )
                .waitUntilComplete()
                .getResult();

        assert template.getName() != null;
    }

}

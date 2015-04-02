package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.template.CreateTemplateCommand;
import com.centurylink.cloud.sdk.servers.services.domain.template.Template;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.core.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.core.TestGroups.LONG_RUNNING;
import static com.centurylink.cloud.sdk.servers.services.domain.template.CreateTemplateCommand.Visibility.PRIVATE;

/**
 * @author ilya.drabenia
 */
@Test(groups = {LONG_RUNNING, INTEGRATION}, enabled = false)
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

package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.services.domain.Server;
import com.centurylink.cloud.sdk.servers.services.domain.template.CreateTemplateCommand;
import com.centurylink.cloud.sdk.servers.services.domain.template.Template;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.servers.services.domain.template.CreateTemplateCommand.Visibility.PRIVATE;

/**
 * @author ilya.drabenia
 */
@Test(groups = "LongRunning")
public class ConvertToTemplateTest extends AbstractServersSdkTest {

    @Inject
    private ServerService serverService;

    @Test
    public void testConvertToTemplate() {
        Server server = new TestServerSupport(serverService).createAnyServer();

        Template template =
            serverService
                .convertToTemplate(new CreateTemplateCommand()
                    .server(server)
                    .description("testTemplate")
                    .visibility(PRIVATE)
                )
                .waitUntilComplete()
                .getResult();

        assert template.getName() != null;
    }

}

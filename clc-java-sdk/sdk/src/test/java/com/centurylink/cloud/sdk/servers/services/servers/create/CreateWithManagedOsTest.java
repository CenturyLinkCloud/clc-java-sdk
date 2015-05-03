package com.centurylink.cloud.sdk.servers.services.servers.create;

import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.template.refs.Template;
import com.google.inject.Inject;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.common.services.services.domain.datacenters.refs.DataCenter.US_EAST_STERLING;
import static com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group.DEFAULT_GROUP;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.CpuArchitecture.x86_64;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.OsType.RHEL;
import static com.centurylink.cloud.sdk.servers.services.servers.TestServerSupport.anyServerConfig;
import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * @author Ilya Drabenia
 */
@Test(groups = {INTEGRATION, LONG_RUNNING})
public class CreateWithManagedOsTest extends AbstractServersSdkTest {

    @Inject
    ServerService serverService;

    ServerMetadata server;

    @Test
    public void testCreateWithManagedOS() {
        server =
            serverService.create(anyServerConfig()
                .name("CMOS")
                .template(Template.refByOs()
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

        assert !isNullOrEmpty(server.getId());
    }

    @AfterMethod
    public void deleteServer() {
        serverService.delete(server.asRefById());
    }

}

package com.centurylink.cloud.sdk.servers.service;

import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.domain.Group;
import com.centurylink.cloud.sdk.servers.domain.Machine;
import com.centurylink.cloud.sdk.servers.domain.Server;
import com.centurylink.cloud.sdk.servers.domain.Template;
import com.centurylink.cloud.sdk.servers.domain.os.OperatingSystem;
import com.google.inject.Inject;
import org.junit.Test;

import static com.centurylink.cloud.sdk.servers.domain.ServerType.STANDARD;
import static com.centurylink.cloud.sdk.servers.domain.datacenter.DataCenters.DE_FRANKFURT;
import static com.centurylink.cloud.sdk.servers.domain.os.CpuArchitecture.x86_64;
import static com.centurylink.cloud.sdk.servers.domain.os.OsType.CENTOS;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * @author ilya.drabenia
 */
public class ServerServiceTest extends AbstractServersSdkTest {

    @Inject
    ServerService serverService;

    @Test
    public void testCreate() throws Exception {
        Server newServer =
            serverService.create(new Server()
                .name("ALTRS1")
                .type(STANDARD)

                .group(new Group()
                    .datacenter(DE_FRANKFURT)
                    .name("Group3")
                )

                .machine(new Machine()
                    .cpuCount(1)
                    .ram(2)
                )

                .template(new Template().os(new OperatingSystem()
                    .type(CENTOS)
                    .version("6")
                    .architecture(x86_64)
                ))
            )

            .waitUntilComplete()
            .getResult();

        assert !isNullOrEmpty(newServer.getId());

        cleanUpCreatedResources(newServer);
    }

    void cleanUpCreatedResources(Server newServer) {
        serverService.delete(newServer);
    }

}

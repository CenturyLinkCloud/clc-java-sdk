package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.servers.services.domain.Machine;
import com.centurylink.cloud.sdk.servers.services.domain.Server;
import com.centurylink.cloud.sdk.servers.services.domain.group.Group;
import com.centurylink.cloud.sdk.servers.services.domain.os.OperatingSystem;
import com.centurylink.cloud.sdk.servers.services.domain.template.Template;

import static com.centurylink.cloud.sdk.servers.services.domain.ServerType.STANDARD;
import static com.centurylink.cloud.sdk.servers.services.domain.datacenter.DataCenters.DE_FRANKFURT;
import static com.centurylink.cloud.sdk.servers.services.domain.os.CpuArchitecture.x86_64;
import static com.centurylink.cloud.sdk.servers.services.domain.os.OsType.CENTOS;

/**
 * @author ilya.drabenia
 */
public class TestServerSupport {
    public static final String PASSWORD = "1qa@WS3ed";
    private final ServerService serverService;

    public TestServerSupport(ServerService serverService) {
        this.serverService = serverService;
    }

    public Server createAnyServer() {
        return
            serverService.create(new Server()
                .name("ALTRS1")
                .type(STANDARD)
                .password(PASSWORD)

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
                ))
            )

            .waitUntilComplete()
            .getResult();
    }

    public void deleteServer(Server newServer) {
        serverService
            .delete(newServer)
            .waitUntilComplete();
    }
}

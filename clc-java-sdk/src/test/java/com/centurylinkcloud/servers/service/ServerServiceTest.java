package com.centurylinkcloud.servers.service;

import com.centurylinkcloud.servers.config.ServersModule;
import com.centurylinkcloud.servers.domain.Group;
import com.centurylinkcloud.servers.domain.Machine;
import com.centurylinkcloud.servers.domain.Server;
import com.centurylinkcloud.servers.domain.Template;
import com.centurylinkcloud.servers.domain.os.OperatingSystem;
import com.google.inject.Guice;
import com.google.inject.Inject;
import org.junit.Before;
import org.junit.Test;

import static com.centurylinkcloud.servers.domain.ServerType.STANDARD;
import static com.centurylinkcloud.servers.domain.datacenter.DataCenters.DE_FRANKFURT;
import static com.centurylinkcloud.servers.domain.os.CpuArchitecture.x86_64;
import static com.centurylinkcloud.servers.domain.os.OsType.CENTOS;

/**
 * @author ilya.drabenia
 */
public class ServerServiceTest {

    @Inject
    private ServerService serverService;

    @Before
    public void setUp() {
        Guice
            .createInjector(new ServersModule())
            .injectMembers(this);
    }

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

        System.out.println(serverService.delete(newServer));
    }

}

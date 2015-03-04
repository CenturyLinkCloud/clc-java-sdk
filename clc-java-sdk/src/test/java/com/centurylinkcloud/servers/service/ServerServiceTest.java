package com.centurylinkcloud.servers.service;

import com.centurylinkcloud.servers.domain.*;
import com.centurylinkcloud.servers.domain.datacenter.DataCenters;
import org.junit.Test;

import static com.centurylinkcloud.servers.domain.InstanceType.STANDARD;

/**
 * @author ilya.drabenia
 */
public class ServerServiceTest {
    private ServerService serverService = new ServerService();

    @Test
    public void testCreate() throws Exception {
        serverService.create("ALTR", new Server()
            .name("ALTRS1")
            .type(STANDARD)
            .group(new Group()
                .datacenter(DataCenters.DE_FRANKFURT)
                .name("Group3")
            )
            .machine(new Machine()
                .cpuCount(1)
                .ram(2)
            )
            .template(new Template()
                .name("CENTOS-6-64-TEMPLATE")
            )
        );
    }

}

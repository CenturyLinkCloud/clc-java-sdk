package com.centurylinkcloud.servers;

import com.centurylinkcloud.servers.client.model.CreateServerCommand;
import com.centurylinkcloud.servers.client.ServerClient;
import org.junit.Test;

/**
 * @author ilya.drabenia
 */
public class ServerClientTest {

    @Test
    public void createServerTest() {
        System.out.println(
            new ServerClient()
                .create("ALTR", new CreateServerCommand()
                    .cpu(1)
                    .groupId("de1-57241")
                    .memoryGB(1)
                    .name("ALTR1")
                    .type("standard")
                    .sourceServerId("CENTOS-6-64-TEMPLATE")
                )
        );
    }

}

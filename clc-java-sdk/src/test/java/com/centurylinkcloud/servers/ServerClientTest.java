package com.centurylinkcloud.servers;

import com.centurylinkcloud.servers.client.domain.CreateServerCommand;
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

    @Test
    public void getDataCenterTest() {
        System.out.println(
            new ServerClient()
                .getDataCenter("ALTR", "DE1").getGroup().getId()
        );
    }

    @Test
    public void getGroupsTest() {
        System.out.println(
            new ServerClient().getGroups("ALTR", "de1-35501").getGroups()
        );
    }

}

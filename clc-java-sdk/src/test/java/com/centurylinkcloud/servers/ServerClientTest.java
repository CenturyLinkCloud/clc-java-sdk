package com.centurylinkcloud.servers;

import com.centurylinkcloud.servers.client.domain.server.CreateServerCommand;
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
                .create(new CreateServerCommand()
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
                .getDataCenter("DE1").getGroup().getId()
        );
    }

    @Test
    public void getGroupsTest() {
        System.out.println(
            new ServerClient().getGroups("de1-35501").getGroups()
        );
    }

    @Test
    public void getDeploymentCapabilitiesTest() {
        System.out.println(
            new ServerClient()
                .getDataCenterDeploymentCapabilities("DE1")
                .getTemplates().get(3).getDescription()
        );
    }

}

package com.centurylinkcloud.servers;

import com.centurylinkcloud.servers.model.CreateServerCommand;

/**
 * @author ilya.drabenia
 */
public class TestService {

    public static void main(String... args) {
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

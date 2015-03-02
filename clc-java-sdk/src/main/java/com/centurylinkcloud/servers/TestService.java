package com.centurylinkcloud.servers;

import com.centurylinkcloud.servers.model.CreateServerCommand;

/**
 * @author ilya.drabenia
 */
public class TestService {

    public static void main(String... args) {
        System.out.println(
            new ServerClient()
                .create("myServer", new CreateServerCommand()
                    .cpu(1)
                    .groupId("A5")
                    .memoryGB(1)
                    .name("server1")
                    .type("standard")
                    .sourceServerId("server0")
                )
        );
    }

}

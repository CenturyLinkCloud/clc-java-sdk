package com.centurylink.cloud.sdk.servers.services.servers.search;

import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ServerReferenceTest {

    @Test
    public void testToServerReferenceStringRepresentation() {
        Server newServer = Server.refById("ID");

        String representation = newServer.toString();

        assertEquals(representation, "{\"id\":\"ID\"}");
    }

}
package com.centurylink.cloud.sdk.servers.services.servers.search;

import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class ServerReferenceTest {

    @Test
    public void testToServerReferenceStringRepresentation() {
        Server newServer = Server.refById("ca2altddeb201");

        String representation = newServer.toString();

        assertTrue(representation.contains("\"id\":\"ca2altddeb201\"}"));
    }

}
package com.centurylink.cloud.sdk.servers.services.domain.server.refs;

import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter.US_WEST_SANTA_CLARA;
import static org.testng.Assert.assertTrue;

public class ServerByDescriptionRefTest {

    @Test
    public void testServerByDescSerialization() {
        Server server = Server.refByDescription(US_WEST_SANTA_CLARA, "My Description");

        String toStringValue = server.toString();

        assertTrue(toStringValue.contains("My Description"));
        assertTrue(toStringValue.contains("uc1"));
    }

}
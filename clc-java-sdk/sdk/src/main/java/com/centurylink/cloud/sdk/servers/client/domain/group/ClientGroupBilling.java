package com.centurylink.cloud.sdk.servers.client.domain.group;

import java.util.HashMap;
import java.util.Map;

public class ClientGroupBilling {

    private String name;
    private Map<String, ClientServerBilling> servers = new HashMap<>();

    public String getName() {
        return name;
    }

    public Map<String, ClientServerBilling> getServers() {
        return servers;
    }
}

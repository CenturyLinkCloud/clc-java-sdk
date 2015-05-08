package com.centurylink.cloud.sdk.servers.client.domain.group;

import java.util.HashMap;
import java.util.Map;

public class GroupBilling {

    private String name;
    private Map<String, ServerBilling> servers = new HashMap<>();

    public String getName() {
        return name;
    }

    public Map<String, ServerBilling> getServers() {
        return servers;
    }
}

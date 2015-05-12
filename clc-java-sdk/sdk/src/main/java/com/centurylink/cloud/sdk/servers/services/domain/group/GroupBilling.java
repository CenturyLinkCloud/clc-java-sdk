package com.centurylink.cloud.sdk.servers.services.domain.group;

import java.util.ArrayList;
import java.util.List;

public class GroupBilling {

    private String groupId;
    private String name;
    private List<ServerBilling> servers = new ArrayList<>();

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public GroupBilling groupId(String groupId) {
        setGroupId(groupId);
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GroupBilling name(String name) {
        setName(name);
        return this;
    }

    public List<ServerBilling> getServers() {
        return servers;
    }

    public void setServers(List<ServerBilling> servers) {
        this.servers = servers;
    }

    public GroupBilling server(ServerBilling server) {
        this.servers.add(server);
        return this;
    }

    public GroupBilling servers(List<ServerBilling> servers) {
        setServers(servers);
        return this;
    }
}

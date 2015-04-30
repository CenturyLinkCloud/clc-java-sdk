package com.centurylink.cloud.sdk.servers.services.domain.server;

import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;

public class ModifyServerConfig {

    private Group group;
    private String description;
    private Machine machineConfig = new Machine();
    private CredentialsConfig credentialsConfig = new CredentialsConfig();

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public ModifyServerConfig group(Group group) {
        setGroup(group);
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ModifyServerConfig description(String description) {
        setDescription(description);
        return this;
    }

    public Machine getMachineConfig() {
        return machineConfig;
    }

    public void setMachineConfig(Machine machineConfig) {
        this.machineConfig = machineConfig;
    }

    public ModifyServerConfig machineConfig(Machine machineConfig) {
        setMachineConfig(machineConfig);
        return this;
    }

    public CredentialsConfig getCredentialsConfig() {
        return credentialsConfig;
    }

    public void setCredentialsConfig(CredentialsConfig credentialsConfig) {
        this.credentialsConfig = credentialsConfig;
    }

    public ModifyServerConfig credentialsConfig(CredentialsConfig credentialsConfig) {
        setCredentialsConfig(credentialsConfig);
        return this;
    }
}

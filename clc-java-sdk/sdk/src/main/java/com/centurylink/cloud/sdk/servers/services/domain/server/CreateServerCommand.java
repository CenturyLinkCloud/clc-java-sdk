package com.centurylink.cloud.sdk.servers.services.domain.server;

import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.template.refs.TemplateRef;

import java.time.ZonedDateTime;


/**
 * @author ilya.drabenia
 */
public class CreateServerCommand {
    private String id;
    private String name;
    private ServerType type;
    private Group group;
    private TemplateRef template;
    private Machine machine = new Machine();
    private String password;
    private NetworkConfig network = new NetworkConfig();
    private TimeToLive timeToLive;
    private boolean managedOS = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CreateServerCommand id(String id) {
        setId(id);
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CreateServerCommand name(String name) {
        setName(name);
        return this;
    }

    public ServerType getType() {
        return type;
    }

    public void setType(ServerType type) {
        this.type = type;
    }

    public CreateServerCommand type(ServerType type) {
        setType(type);
        return this;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public CreateServerCommand group(Group group) {
        setGroup(group);
        return this;
    }

    public TemplateRef getTemplate() {
        return template;
    }

    public void setTemplate(TemplateRef template) {
        this.template = template;
    }

    public CreateServerCommand template(TemplateRef template) {
        setTemplate(template);
        return this;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public CreateServerCommand machine(Machine machine) {
        setMachine(machine);
        return this;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CreateServerCommand password(String password) {
        setPassword(password);
        return this;
    }

    public NetworkConfig getNetwork() {
        return network;
    }

    public void setNetwork(NetworkConfig network) {
        this.network = network;
    }

    public CreateServerCommand network(NetworkConfig network) {
        setNetwork(network);
        return this;
    }

    public TimeToLive getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(TimeToLive timeToLive) {
        this.timeToLive = timeToLive;
    }

    public CreateServerCommand timeToLive(TimeToLive timeToLive) {
        setTimeToLive(timeToLive);
        return this;
    }

    public CreateServerCommand timeToLive(ZonedDateTime expirationTime) {
        setTimeToLive(new TimeToLive(expirationTime));
        return this;
    }

    public boolean isManagedOS() {
        return managedOS;
    }

    public void setManagedOS(boolean managedOS) {
        this.managedOS = managedOS;
    }

    public CreateServerCommand managedOs() {
        setManagedOS(true);
        return this;
    }
}

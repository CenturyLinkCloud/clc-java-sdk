package com.centurylink.cloud.sdk.servers.services.domain;

import com.centurylink.cloud.sdk.servers.services.domain.group.Group;
import com.centurylink.cloud.sdk.servers.services.domain.template.Template;

/**
 * @author ilya.drabenia
 */
public class Server {
    private String id;
    private String name;
    private ServerType type;
    private Group group;
    private Template template;
    private Machine machine;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Server id(String id) {
        setId(id);
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Server name(String name) {
        setName(name);
        return this;
    }

    public ServerType getType() {
        return type;
    }

    public void setType(ServerType type) {
        this.type = type;
    }

    public Server type(ServerType type) {
        setType(type);
        return this;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Server group(Group group) {
        setGroup(group);
        return this;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public Server template(Template template) {
        setTemplate(template);
        return this;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public Server machine(Machine machine) {
        setMachine(machine);
        return this;
    }
}

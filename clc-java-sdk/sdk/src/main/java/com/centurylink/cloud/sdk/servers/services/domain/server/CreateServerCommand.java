package com.centurylink.cloud.sdk.servers.services.domain.server;

import com.centurylink.cloud.sdk.servers.services.domain.Machine;
import com.centurylink.cloud.sdk.servers.services.domain.group.Group;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.GroupRef;
import com.centurylink.cloud.sdk.servers.services.domain.template.Template;

/**
 * @author ilya.drabenia
 */
public class CreateServerCommand {
    private String id;
    private String name;
    private ServerType type;
    private GroupRef group;
    private Template template;
    private Machine machine;
    private String password;

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

    public GroupRef getGroup() {
        return group;
    }

    public void setGroup(GroupRef group) {
        this.group = group;
    }

    public CreateServerCommand group(GroupRef group) {
        setGroup(group);
        return this;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public CreateServerCommand template(Template template) {
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
}

package com.centurylinkcloud.sample.port.adapter.web.beans;

import com.centurylinkcloud.servers.domain.Group;
import com.centurylinkcloud.servers.domain.Machine;
import com.centurylinkcloud.servers.domain.Server;
import com.centurylinkcloud.servers.domain.ServerType;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author ilya.drabenia
 */
public class ServerBean {
    @JsonIgnore
    private Server server = new Server()
        .machine(new Machine())
        .group(new Group());

    public ServerBean(Server server) {
        this.server = server;
    }

    public ServerBean() {
    }

    public String getName() {
        return server.getName();
    }

    public void setName(String name) {
        server.setName(name);
    }

    public String getId() {
        return server.getId();
    }

    public void setId(String id) {
        server.setId(id);
    }

    public ServerType getType() {
        return server.getType();
    }

    public void setType(ServerType type) {
        server.setType(type);
    }

    public Integer getCpu() {
        return server.getMachine().getCpuCount();
    }

    public void setCpu(Integer count) {
        server.getMachine().setCpuCount(count);
    }

    public Integer getRam() {
        return server.getMachine().getRam();
    }

    public void setRam(Integer ram) {
        server.getMachine().setRam(ram);
    }

    public String getGroup() {
        return server.getGroup().getName();
    }

    public void setGroup(String group) {
        server.getGroup().setName(group);
    }

    public String getDataCenter() {
        return server.getGroup().getDatacenter();
    }

    public void setDataCenter(String dataCenter) {
        server.getGroup().setDatacenter(dataCenter);
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }
}

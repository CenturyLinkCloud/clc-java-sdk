package com.centurylink.cloud.sdk.sample.port.adapter.web.beans;

import com.centurylink.cloud.sdk.core.datacenters.services.domain.datacenter.DataCenter;
import com.centurylink.cloud.sdk.core.datacenters.services.domain.datacenter.refs.IdDataCenterRef;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.IdGroupRef;
import com.centurylink.cloud.sdk.servers.services.domain.server.CreateServerCommand;
import com.centurylink.cloud.sdk.servers.services.domain.server.Machine;
import com.centurylink.cloud.sdk.servers.services.domain.server.Network;
import com.centurylink.cloud.sdk.servers.services.domain.server.ServerType;
import com.centurylink.cloud.sdk.servers.services.domain.template.refs.NameTemplateRef;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author ilya.drabenia
 */
public class ServerBean {
    @JsonIgnore
    private CreateServerCommand server = new CreateServerCommand()
        .machine(new Machine())
        .network(new Network())
        .group(new IdGroupRef(null, null))
        .template(new NameTemplateRef(null, null));

    public ServerBean(CreateServerCommand server) {
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
        return server.getGroup().as(IdGroupRef.class).getId();
    }

    public void setGroup(String group) {
        server.group(
            server.getGroup().as(IdGroupRef.class).id(group)
        );
    }

    public String getDataCenter() {
        return server
            .getGroup().as(IdGroupRef.class)
            .getDataCenter().as(IdDataCenterRef.class)
            .getId();
    }

    public void setDataCenter(String dataCenter) {
        server.group(
            server
                .getGroup().as(IdGroupRef.class)
                .dataCenter(DataCenter.refById(dataCenter))
        );

        server.template(
            server
                .getTemplate().as(NameTemplateRef.class)
                .dataCenter(DataCenter.refById(dataCenter))
        );
    }

    public CreateServerCommand getServer() {
        return server;
    }

    public void setServer(CreateServerCommand server) {
        this.server = server;
    }

    public String getTemplate() {
        return server.getTemplate().as(NameTemplateRef.class).getName();
    }

    public void setTemplate(String template) {
        server.template(
            server.getTemplate().as(NameTemplateRef.class).name(template)
        );
    }
}

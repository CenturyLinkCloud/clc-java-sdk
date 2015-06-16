/*
 * (c) 2015 CenturyLink. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.centurylink.cloud.sdk.sample.port.adapter.web.beans;

import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenterByIdRef;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.GroupByIdRef;
import com.centurylink.cloud.sdk.servers.services.domain.server.CreateServerConfig;
import com.centurylink.cloud.sdk.servers.services.domain.server.Machine;
import com.centurylink.cloud.sdk.servers.services.domain.server.NetworkConfig;
import com.centurylink.cloud.sdk.servers.services.domain.server.ServerType;
import com.centurylink.cloud.sdk.servers.services.domain.server.TimeToLive;
import com.centurylink.cloud.sdk.servers.services.domain.template.refs.Template;
import com.centurylink.cloud.sdk.servers.services.domain.template.refs.TemplateByNameRef;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author ilya.drabenia
 */
public class ServerBean {
    @JsonIgnore
    private CreateServerConfig server = new CreateServerConfig()
        .machine(new Machine())
        .network(new NetworkConfig())
        .group(Group.refByName())
        .template(Template.refByName());

    public ServerBean(CreateServerConfig server) {
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
        return server.getGroup().as(GroupByIdRef.class).getId();
    }

    public void setGroup(String group) {
        server.group(
            Group.refById(group)
        );
    }

    public String getDataCenter() {
        return server
            .getTemplate()
            .getDataCenter().as(DataCenterByIdRef.class)
            .getId();
    }

    public void setDataCenter(String dataCenter) {
        server.template(
            server
                .getTemplate().as(TemplateByNameRef.class)
                .dataCenter(DataCenter.refById(dataCenter))
        );
    }

    public CreateServerConfig getServer() {
        return server;
    }

    public void setServer(CreateServerConfig server) {
        this.server = server;
    }

    public String getTemplate() {
        return server.getTemplate().as(TemplateByNameRef.class).getName();
    }

    public void setTemplate(String template) {
        server.template(
            server.getTemplate().as(TemplateByNameRef.class).name(template)
        );
    }

    public TimeToLive getTimeToLive() {
        return server.getTimeToLive();
    }

    public void setTimeToLive(TimeToLive timeToLive) {

    }
}

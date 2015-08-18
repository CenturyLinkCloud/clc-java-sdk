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

package com.centurylink.cloud.sdk.server.services.dsl.domain.server;

import com.centurylink.cloud.sdk.policy.services.dsl.domain.refs.AntiAffinityPolicy;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group;
import com.centurylink.cloud.sdk.server.services.dsl.domain.template.refs.Template;

import java.time.ZonedDateTime;


/**
 * @author ilya.drabenia
 */
public class CreateServerConfig implements ServerConfig {
    private String id;
    private String name;
    private String description;
    private ServerType type = ServerType.STANDARD;
    private StorageType storageType = StorageType.STANDARD;
    private Group group;
    private Template template;
    private Machine machine = new Machine();
    private String password;
    private NetworkConfig network = new NetworkConfig();
    private TimeToLive timeToLive;
    private boolean managedOS = false;
    private AntiAffinityPolicy antiAffinityPolicy;

    public CompositeServerConfig count(int count) {
        return new CompositeServerConfig().server(this).count(count);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CreateServerConfig id(String id) {
        setId(id);
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CreateServerConfig description(String description) {
        setDescription(description);
        return this;
    }

    public CreateServerConfig name(String name) {
        setName(name);
        return this;
    }

    public ServerType getType() {
        return type;
    }

    public void setType(ServerType type) {
        this.type = type;
    }

    public CreateServerConfig type(ServerType type) {
        setType(type);
        return this;
    }

    public StorageType getStorageType() {
        return storageType;
    }

    public CreateServerConfig storageType(StorageType storageType) {
        this.storageType = storageType;
        return this;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public CreateServerConfig group(Group group) {
        setGroup(group);
        return this;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public CreateServerConfig template(Template template) {
        setTemplate(template);
        return this;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public CreateServerConfig machine(Machine machine) {
        setMachine(machine);
        return this;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CreateServerConfig password(String password) {
        setPassword(password);
        return this;
    }

    public NetworkConfig getNetwork() {
        return network;
    }

    public void setNetwork(NetworkConfig network) {
        this.network = network;
    }

    public CreateServerConfig network(NetworkConfig network) {
        setNetwork(network);
        return this;
    }

    public TimeToLive getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(TimeToLive timeToLive) {
        this.timeToLive = timeToLive;
    }

    public CreateServerConfig timeToLive(TimeToLive timeToLive) {
        setTimeToLive(timeToLive);
        return this;
    }

    public CreateServerConfig timeToLive(ZonedDateTime expirationTime) {
        setTimeToLive(new TimeToLive(expirationTime));
        return this;
    }

    public boolean isManagedOS() {
        return managedOS;
    }

    public void setManagedOS(boolean managedOS) {
        this.managedOS = managedOS;
    }

    public CreateServerConfig managedOs() {
        setManagedOS(true);
        return this;
    }

    public AntiAffinityPolicy getAntiAffinityPolicy() {
        return antiAffinityPolicy;
    }

    public CreateServerConfig antiAffinityPolicy(AntiAffinityPolicy antiAffinityPolicy) {
        this.antiAffinityPolicy = antiAffinityPolicy;
        return this;
    }

    @Override
    public CreateServerConfig[] getServerConfig() {
        return new CreateServerConfig[]{this};
    }
}

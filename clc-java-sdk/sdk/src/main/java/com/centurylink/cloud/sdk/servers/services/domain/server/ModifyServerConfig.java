/*
 * (c) 2015 CenturyLink Cloud. All Rights Reserved.
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

package com.centurylink.cloud.sdk.servers.services.domain.server;

import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;

public class ModifyServerConfig {

    private String groupId;
    private String description;
    private Machine machineConfig = new Machine();
    private CredentialsConfig credentialsConfig = new CredentialsConfig();

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String group) {
        this.groupId = group;
    }

    public ModifyServerConfig groupId(String group) {
        setGroupId(group);
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

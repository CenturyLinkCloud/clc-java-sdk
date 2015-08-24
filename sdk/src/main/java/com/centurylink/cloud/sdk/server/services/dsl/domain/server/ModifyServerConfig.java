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

import com.centurylink.cloud.sdk.server.services.client.domain.server.CustomField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class ModifyServerConfig {

    private String groupId;
    private String description;
    private Machine machineConfig = new Machine();
    private CredentialsConfig credentialsConfig = new CredentialsConfig();
    private List<CustomField> customFields = new ArrayList<>();

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

    public List<CustomField> getCustomFields() {
        return customFields;
    }

    public ModifyServerConfig customFields(List<CustomField> customFields) {
        checkNotNull(customFields, "List of custom fields must be not a null");
        this.customFields = customFields;
        return this;
    }

    public ModifyServerConfig customFields(CustomField... customFields) {
        checkNotNull(customFields, "List of custom fields must be not a null");
        this.customFields.addAll(Arrays.asList(customFields));
        return this;
    }
}

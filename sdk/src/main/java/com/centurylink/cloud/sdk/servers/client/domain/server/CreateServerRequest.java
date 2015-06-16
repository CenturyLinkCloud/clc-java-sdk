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

package com.centurylink.cloud.sdk.servers.client.domain.server;

import com.centurylink.cloud.sdk.servers.services.domain.server.ServerType;
import com.centurylink.cloud.sdk.servers.services.domain.server.StorageType;
import com.centurylink.cloud.sdk.servers.services.domain.server.TimeToLive;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class CreateServerRequest {
    private String name;

    private String description;

    private String groupId;

    private String sourceServerId;

    private Integer cpu;

    private Integer memoryGB;

    private String type;

    private String storageType;

    private String password;

    private String primaryDns;

    private String secondaryDns;

    private String networkId;

    private List<DiskRequest> additionalDisks = new ArrayList<>();

    private String ttl;

    @JsonProperty("isManagedOS")
    private boolean isManagedOS;

    private boolean isManagedBackup;


    /**
     * Name of the server to create. Alphanumeric characters and dashes only.
     * Must be between 1-7 characters depending on the length of the account alias.
     * (This name will be appended with a two digit number and prepended with the dataCenter code
     * and account alias to make up the final server name.)
     *
     * @param name the server name
     * @return current instance
     */
    public CreateServerRequest name(String name) {
        this.name = name;
        return this;
    }

    /**
     * ID of the parent group. Retrieved from query to parent group,
     * or by looking at the URL on the UI pages in the Control Portal.
     *
     * @param groupId the group id
     * @return current instance
     */
    public CreateServerRequest groupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    /**
     * ID of the server to use a source. May be the ID of a template, or when cloning,
     * an existing server ID. The list of available templates for a given account in a data center
     * can be retrieved from the Get Data Center Deployment Capabilities API operation.
     *
     * @param sourceServerId the source server id
     * @return current instance
     */
    public CreateServerRequest sourceServerId(String sourceServerId) {
        this.sourceServerId = sourceServerId;
        return this;
    }

    /**
     * Number of processors to configure the server with (1-16)
     *
     * @param cpu the number of CPU
     * @return current instance
     */
    public CreateServerRequest cpu(Integer cpu) {
        this.cpu = cpu;
        return this;
    }

    /**
     * Number of GB of memory to configure the server with (1-128)
     *
     * @param memoryGB the number of memory
     * @return current instance
     */
    public CreateServerRequest memoryGB(Integer memoryGB) {
        this.memoryGB = memoryGB;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CreateServerRequest description(String description) {
        setDescription(description);
        return this;
    }

    /**
     * Name of the server to create. Alphanumeric characters and dashes only.
     * Must be between 1-7 characters depending on the length of the account alias.
     * (This name will be appended with a two digit number and prepended with the dataCenter code
     * and account alias to make up the final server name.)
     *
     * @return the server name
     */
    public String getName() {
        return name;
    }

    /**
     * ID of the parent group. Retrieved from query to parent group,
     * or by looking at the URL on the UI pages in the Control Portal.
     *
     * @return the group id
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * ID of the server to use a source. May be the ID of a template, or when cloning,
     * an existing server ID. The list of available templates for a given account in a data center
     * can be retrieved from the Get Data Center Deployment Capabilities API operation.
     *
     * @return the source server id
     */
    public String getSourceServerId() {
        return sourceServerId;
    }

    /**
     * Number of processors to configure the server with (1-16)
     *
     * @return the number of CPU
     */
    public Integer getCpu() {
        return cpu;
    }

    /**
     * Number of GB of memory to configure the server with (1-128)
     *
     * @return the number of RAM
     */
    public Integer getMemoryGB() {
        return memoryGB;
    }

    /**
     * Whether to create standard or hyperscale server
     *
     * @return the server type
     */
    public String getType() {
        return type;
    }

    /**
     * Whether to create standard or hyperscale server
     *
     * @param type the server type
     * @param hasCapability the flag, that shows the availability of capability
     * @return current instance
     */
    public CreateServerRequest type(String type, boolean hasCapability) {
        if (ServerType.HYPERSCALE.getCode().equals(type) && !hasCapability) {
            throw new IllegalArgumentException("Hyperscale server type is not available in selected data center");
        }
        this.type = type;
        return this;
    }

    /**
     * Whether to create standard or premium storage
     *
     * @return the storage type
     */
    public String getStorageType() {
        return storageType;
    }

    /**
     * Whether to create standard or premium storage
     *
     * @param storageType the storage type
     * @param hasCapability the flag, that shows the availability of capability
     * @return current instance
     */
    public CreateServerRequest storageType(String storageType, boolean hasCapability) {
        if (StorageType.PREMIUM.getCode().equals(storageType) && !hasCapability) {
            throw new IllegalArgumentException("Premium storage type is not supported by this datacenter");
        }
        this.storageType = storageType;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public CreateServerRequest password(String password) {
        this.password = password;
        return this;
    }

    public String getPrimaryDns() {
        return primaryDns;
    }

    public void setPrimaryDns(String primaryDns) {
        this.primaryDns = primaryDns;
    }

    public CreateServerRequest primaryDns(String primaryDns) {
        setPrimaryDns(primaryDns);
        return this;
    }

    public String getSecondaryDns() {
        return secondaryDns;
    }

    public void setSecondaryDns(String secondaryDns) {
        this.secondaryDns = secondaryDns;
    }

    public CreateServerRequest secondaryDns(String secondaryDns) {
        setSecondaryDns(secondaryDns);
        return this;
    }

    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    public CreateServerRequest networkId(String networkId) {
        setNetworkId(networkId);
        return this;
    }

    public List<DiskRequest> getAdditionalDisks() {
        return additionalDisks;
    }

    public void setAdditionalDisks(List<DiskRequest> additionalDisks) {
        this.additionalDisks = additionalDisks;
    }

    public CreateServerRequest additionalDisks(List<DiskRequest> disks) {
        setAdditionalDisks(disks);
        return this;
    }

    /**
     * Time to Live the server
     *
     * @return the time to live
     */
    public String getTtl() {
        return ttl;
    }

    /**
     * Time to Live the server
     *
     * @param timeToLive the time to live
     * @return current instance
     */
    public CreateServerRequest timeToLive(TimeToLive timeToLive) {
        if (timeToLive != null) {
            this.ttl = timeToLive.format();
        }
        return this;
    }

    public boolean isManagedOS() {
        return isManagedOS;
    }

    public CreateServerRequest managedOS(boolean isManagedOS, boolean hasTemplateCapability) {
        if (isManagedOS && !hasTemplateCapability) {
            throw new IllegalArgumentException("Managed OS capabilities is not supported by this template");
        }

        this.isManagedOS = isManagedOS;
        return this;
    }
}

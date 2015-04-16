package com.centurylink.cloud.sdk.servers.client.domain.server;

import com.centurylink.cloud.sdk.servers.services.domain.server.TimeToLive;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class CreateServerRequest {
    private String name;

    private String groupId;

    private String sourceServerId;

    private Integer cpu;

    private Integer memoryGB;

    private String type;

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
     * Name of the server to create. Alphanumeric characters and dashes only. Must be between 1-7 characters depending on the length of the account alias. (This name will be appended with a two digit number and prepended with the dataCenter code and account alias to make up the final server name.)
     */
    public CreateServerRequest name(String name) {
        this.name = name;
        return this;
    }

    /**
     * ID of the parent group. Retrieved from query to parent group, or by looking at the URL on the UI pages in the Control Portal.
     */
    public CreateServerRequest groupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    /**
     * ID of the server to use a source. May be the ID of a template, or when cloning, an existing server ID. The list of available templates for a given account in a data center can be retrieved from the Get Data Center Deployment Capabilities API operation.
     */
    public CreateServerRequest sourceServerId(String sourceServerId) {
        this.sourceServerId = sourceServerId;
        return this;
    }

    /**
     * Number of processors to configure the server with (1-16)
     */
    public CreateServerRequest cpu(Integer cpu) {
        this.cpu = cpu;
        return this;
    }

    /**
     * Number of GB of memory to configure the server with (1-128)
     */
    public CreateServerRequest memoryGB(Integer memoryGB) {
        this.memoryGB = memoryGB;
        return this;
    }

    /**
     * Whether to create standard or hyperscale server
     */
    public CreateServerRequest type(String type) {
        this.type = type;
        return this;
    }



    /**
     * Name of the server to create. Alphanumeric characters and dashes only. Must be between 1-7 characters depending on the length of the account alias. (This name will be appended with a two digit number and prepended with the dataCenter code and account alias to make up the final server name.)
     */
    public String getName() {
        return name;
    }

    /**
     * ID of the parent group. Retrieved from query to parent group, or by looking at the URL on the UI pages in the Control Portal.
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * ID of the server to use a source. May be the ID of a template, or when cloning, an existing server ID. The list of available templates for a given account in a data center can be retrieved from the Get Data Center Deployment Capabilities API operation.
     */
    public String getSourceServerId() {
        return sourceServerId;
    }

    /**
     * Number of processors to configure the server with (1-16)
     */
    public Integer getCpu() {
        return cpu;
    }

    /**
     * Number of GB of memory to configure the server with (1-128)
     */
    public Integer getMemoryGB() {
        return memoryGB;
    }

    /**
     * Whether to create standard or hyperscale server
     */
    public String getType() {
        return type;
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
     */
    public String getTtl() {
        return ttl;
    }

    /**
     * Time to Live the server
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
            throw new IllegalArgumentException("The template can't manage OS");
        }
        this.isManagedOS = isManagedOS;
        return this;
    }
}

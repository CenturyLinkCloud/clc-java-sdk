package com.centurylinkcloud.servers.client.domain.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "ipAddresses",
        "alertPolicies",
        "cpu",
        "diskCount",
        "hostName",
        "inMaintenanceMode",
        "memoryMB",
        "powerState",
        "storageGB",
        "disks",
        "partitions",
        "snapshots",
        "customFields"
})
public class Details {

    @JsonProperty("ipAddresses")
    private List<IpAddress> ipAddresses = new ArrayList<IpAddress>();
    @JsonProperty("alertPolicies")
    private List<Object> alertPolicies = new ArrayList<Object>();
    @JsonProperty("cpu")
    private Integer cpu;
    @JsonProperty("diskCount")
    private Integer diskCount;
    @JsonProperty("hostName")
    private String hostName;
    @JsonProperty("inMaintenanceMode")
    private Boolean inMaintenanceMode;
    @JsonProperty("memoryMB")
    private Integer memoryMB;
    @JsonProperty("powerState")
    private String powerState;
    @JsonProperty("storageGB")
    private Integer storageGB;
    @JsonProperty("disks")
    private List<Disk> disks = new ArrayList<Disk>();
    @JsonProperty("partitions")
    private List<Partition> partitions = new ArrayList<Partition>();
    @JsonProperty("snapshots")
    private List<Object> snapshots = new ArrayList<Object>();
    @JsonProperty("customFields")
    private List<Object> customFields = new ArrayList<Object>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The ipAddresses
     */
    @JsonProperty("ipAddresses")
    public List<IpAddress> getIpAddresses() {
        return ipAddresses;
    }

    /**
     *
     * @param ipAddresses
     * The ipAddresses
     */
    @JsonProperty("ipAddresses")
    public void setIpAddresses(List<IpAddress> ipAddresses) {
        this.ipAddresses = ipAddresses;
    }

    /**
     *
     * @return
     * The alertPolicies
     */
    @JsonProperty("alertPolicies")
    public List<Object> getAlertPolicies() {
        return alertPolicies;
    }

    /**
     *
     * @param alertPolicies
     * The alertPolicies
     */
    @JsonProperty("alertPolicies")
    public void setAlertPolicies(List<Object> alertPolicies) {
        this.alertPolicies = alertPolicies;
    }

    /**
     *
     * @return
     * The cpu
     */
    @JsonProperty("cpu")
    public Integer getCpu() {
        return cpu;
    }

    /**
     *
     * @param cpu
     * The cpu
     */
    @JsonProperty("cpu")
    public void setCpu(Integer cpu) {
        this.cpu = cpu;
    }

    /**
     *
     * @return
     * The diskCount
     */
    @JsonProperty("diskCount")
    public Integer getDiskCount() {
        return diskCount;
    }

    /**
     *
     * @param diskCount
     * The diskCount
     */
    @JsonProperty("diskCount")
    public void setDiskCount(Integer diskCount) {
        this.diskCount = diskCount;
    }

    /**
     *
     * @return
     * The hostName
     */
    @JsonProperty("hostName")
    public String getHostName() {
        return hostName;
    }

    /**
     *
     * @param hostName
     * The hostName
     */
    @JsonProperty("hostName")
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     *
     * @return
     * The inMaintenanceMode
     */
    @JsonProperty("inMaintenanceMode")
    public Boolean getInMaintenanceMode() {
        return inMaintenanceMode;
    }

    /**
     *
     * @param inMaintenanceMode
     * The inMaintenanceMode
     */
    @JsonProperty("inMaintenanceMode")
    public void setInMaintenanceMode(Boolean inMaintenanceMode) {
        this.inMaintenanceMode = inMaintenanceMode;
    }

    /**
     *
     * @return
     * The memoryMB
     */
    @JsonProperty("memoryMB")
    public Integer getMemoryMB() {
        return memoryMB;
    }

    /**
     *
     * @param memoryMB
     * The memoryMB
     */
    @JsonProperty("memoryMB")
    public void setMemoryMB(Integer memoryMB) {
        this.memoryMB = memoryMB;
    }

    /**
     *
     * @return
     * The powerState
     */
    @JsonProperty("powerState")
    public String getPowerState() {
        return powerState;
    }

    /**
     *
     * @param powerState
     * The powerState
     */
    @JsonProperty("powerState")
    public void setPowerState(String powerState) {
        this.powerState = powerState;
    }

    /**
     *
     * @return
     * The storageGB
     */
    @JsonProperty("storageGB")
    public Integer getStorageGB() {
        return storageGB;
    }

    /**
     *
     * @param storageGB
     * The storageGB
     */
    @JsonProperty("storageGB")
    public void setStorageGB(Integer storageGB) {
        this.storageGB = storageGB;
    }

    /**
     *
     * @return
     * The disks
     */
    @JsonProperty("disks")
    public List<Disk> getDisks() {
        return disks;
    }

    /**
     *
     * @param disks
     * The disks
     */
    @JsonProperty("disks")
    public void setDisks(List<Disk> disks) {
        this.disks = disks;
    }

    /**
     *
     * @return
     * The partitions
     */
    @JsonProperty("partitions")
    public List<Partition> getPartitions() {
        return partitions;
    }

    /**
     *
     * @param partitions
     * The partitions
     */
    @JsonProperty("partitions")
    public void setPartitions(List<Partition> partitions) {
        this.partitions = partitions;
    }

    /**
     *
     * @return
     * The snapshots
     */
    @JsonProperty("snapshots")
    public List<Object> getSnapshots() {
        return snapshots;
    }

    /**
     *
     * @param snapshots
     * The snapshots
     */
    @JsonProperty("snapshots")
    public void setSnapshots(List<Object> snapshots) {
        this.snapshots = snapshots;
    }

    /**
     *
     * @return
     * The customFields
     */
    @JsonProperty("customFields")
    public List<Object> getCustomFields() {
        return customFields;
    }

    /**
     *
     * @param customFields
     * The customFields
     */
    @JsonProperty("customFields")
    public void setCustomFields(List<Object> customFields) {
        this.customFields = customFields;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
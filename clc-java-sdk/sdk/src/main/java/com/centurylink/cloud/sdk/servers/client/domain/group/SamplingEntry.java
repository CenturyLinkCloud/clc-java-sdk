package com.centurylink.cloud.sdk.servers.client.domain.group;

import com.centurylink.cloud.sdk.core.config.OffsetDateTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author aliaksandr.krasitski
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SamplingEntry {

    @JsonDeserialize(using = OffsetDateTimeDeserializer.class)
    private OffsetDateTime timestamp;
    private Integer cpu = 0;
    private Double cpuPercent = 0d;
    private Integer memoryMB = 0;
    private Double memoryPercent = 0d;
    @JsonProperty(value = "networkReceivedKBps")
    private Double networkReceivedKBps = 0d;
    @JsonProperty(value = "networkTransmittedKBps")
    private Double networkTransmittedKBps = 0d;
    private Integer diskUsageTotalCapacityMB = 0;
    private List<DiskUsageMetadata> diskUsage;
    private List<GuestUsageMetadata> guestDiskUsage;


    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getCpu() {
        return cpu;
    }

    public void setCpu(Integer cpu) {
        this.cpu = cpu;
    }

    public Double getCpuPercent() {
        return cpuPercent;
    }

    public void setCpuPercent(Double cpuPercent) {
        this.cpuPercent = cpuPercent;
    }

    public Integer getMemoryMB() {
        return memoryMB;
    }

    public void setMemoryMB(Integer memoryMB) {
        this.memoryMB = memoryMB;
    }

    public Double getMemoryPercent() {
        return memoryPercent;
    }

    public void setMemoryPercent(Double memoryPercent) {
        this.memoryPercent = memoryPercent;
    }

    public Double getNetworkReceivedKbps() {
        return networkReceivedKBps;
    }

    public void setNetworkReceivedKbps(Double networkReceivedKBps) {
        this.networkReceivedKBps = networkReceivedKBps;
    }

    public Double getNetworkTransmittedKbps() {
        return networkTransmittedKBps;
    }

    public void setNetworkTransmittedKbps(Double networkTransmittedKbps) {
        this.networkTransmittedKBps = networkTransmittedKbps;
    }

    public Integer getDiskUsageTotalCapacityMB() {
        return diskUsageTotalCapacityMB;
    }

    public void setDiskUsageTotalCapacityMB(Integer diskUsageTotalCapacityMB) {
        this.diskUsageTotalCapacityMB = diskUsageTotalCapacityMB;
    }

    public List<DiskUsageMetadata> getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(List<DiskUsageMetadata> diskUsage) {
        this.diskUsage = diskUsage;
    }

    public List<GuestUsageMetadata> getGuestDiskUsage() {
        return guestDiskUsage;
    }

    public void setGuestDiskUsage(List<GuestUsageMetadata> guestDiskUsage) {
        this.guestDiskUsage = guestDiskUsage;
    }
}

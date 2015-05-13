package com.centurylink.cloud.sdk.servers.client.domain.group;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.temporal.Temporal;
import java.util.List;

/**
 * @author aliaksandr.krasitski
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServerMonitoringStats {
    private Temporal timestamp;
    private Float cpu;
    private Float cpuPercent;
    private Float memoryMB;
    private Float memoryPercent;
    private Float networkReceivedKbps;
    private Float networkTransmittedKbps;
    private Float diskUsageTotalCapacityMB;
    private List<DiskUsage> diskUsage;
    private List<GuestUsage> guestDiskUsage;


    public Temporal getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Temporal timestamp) {
        this.timestamp = timestamp;
    }

    public Float getCpu() {
        return cpu;
    }

    public void setCpu(Float cpu) {
        this.cpu = cpu;
    }

    public Float getCpuPercent() {
        return cpuPercent;
    }

    public void setCpuPercent(Float cpuPercent) {
        this.cpuPercent = cpuPercent;
    }

    public Float getMemoryMB() {
        return memoryMB;
    }

    public void setMemoryMB(Float memoryMB) {
        this.memoryMB = memoryMB;
    }

    public Float getMemoryPercent() {
        return memoryPercent;
    }

    public void setMemoryPercent(Float memoryPercent) {
        this.memoryPercent = memoryPercent;
    }

    public Float getNetworkReceivedKbps() {
        return networkReceivedKbps;
    }

    public void setNetworkReceivedKbps(Float networkReceivedKbps) {
        this.networkReceivedKbps = networkReceivedKbps;
    }

    public Float getNetworkTransmittedKbps() {
        return networkTransmittedKbps;
    }

    public void setNetworkTransmittedKbps(Float networkTransmittedKbps) {
        this.networkTransmittedKbps = networkTransmittedKbps;
    }

    public Float getDiskUsageTotalCapacityMB() {
        return diskUsageTotalCapacityMB;
    }

    public void setDiskUsageTotalCapacityMB(Float diskUsageTotalCapacityMB) {
        this.diskUsageTotalCapacityMB = diskUsageTotalCapacityMB;
    }

    public List<DiskUsage> getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(List<DiskUsage> diskUsage) {
        this.diskUsage = diskUsage;
    }

    public List<GuestUsage> getGuestDiskUsage() {
        return guestDiskUsage;
    }

    public void setGuestDiskUsage(List<GuestUsage> guestDiskUsage) {
        this.guestDiskUsage = guestDiskUsage;
    }
}

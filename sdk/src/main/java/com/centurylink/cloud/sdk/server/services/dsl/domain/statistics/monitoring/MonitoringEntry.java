package com.centurylink.cloud.sdk.server.services.dsl.domain.statistics.monitoring;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author aliaksandr.krasitski
 */
public class MonitoringEntry {
    private OffsetDateTime timestamp;
    private Integer cpu;
    private Double cpuPercent;
    private Integer memoryMB;
    private Double memoryPercent;
    private Double networkReceivedKBps;
    private Double networkTransmittedKBps;
    private Integer diskUsageTotalCapacityMB;
    private List<DiskUsage> diskUsage;
    private List<GuestUsage> guestDiskUsage;

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public MonitoringEntry timestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public Integer getCpu() {
        return cpu;
    }

    public MonitoringEntry cpu(Integer cpu) {
        this.cpu = cpu;
        return this;
    }

    public Double getCpuPercent() {
        return cpuPercent;
    }

    public MonitoringEntry cpuPercent(Double cpuPercent) {
        this.cpuPercent = cpuPercent;
        return this;
    }

    public Integer getMemoryMB() {
        return memoryMB;
    }

    public MonitoringEntry memoryMB(Integer memoryMB) {
        this.memoryMB = memoryMB;
        return this;
    }

    public Double getMemoryPercent() {
        return memoryPercent;
    }

    public MonitoringEntry memoryPercent(Double memoryPercent) {
        this.memoryPercent = memoryPercent;
        return this;
    }

    public Double getNetworkReceivedKBps() {
        return networkReceivedKBps;
    }

    public MonitoringEntry networkReceivedKBps(Double networkReceivedKBps) {
        this.networkReceivedKBps = networkReceivedKBps;
        return this;
    }

    public Double getNetworkTransmittedKBps() {
        return networkTransmittedKBps;
    }

    public MonitoringEntry networkTransmittedKBps(Double networkTransmittedKBps) {
        this.networkTransmittedKBps = networkTransmittedKBps;
        return this;
    }

    public Integer getDiskUsageTotalCapacityMB() {
        return diskUsageTotalCapacityMB;
    }

    public MonitoringEntry diskUsageTotalCapacityMB(Integer diskUsageTotalCapacityMB) {
        this.diskUsageTotalCapacityMB = diskUsageTotalCapacityMB;
        return this;
    }

    public List<DiskUsage> getDiskUsage() {
        return diskUsage;
    }

    public MonitoringEntry diskUsage(List<DiskUsage> diskUsage) {
        this.diskUsage = diskUsage;
        return this;
    }

    public List<GuestUsage> getGuestDiskUsage() {
        return guestDiskUsage;
    }

    public MonitoringEntry guestDiskUsage(List<GuestUsage> guestDiskUsage) {
        this.guestDiskUsage = guestDiskUsage;
        return this;
    }
}

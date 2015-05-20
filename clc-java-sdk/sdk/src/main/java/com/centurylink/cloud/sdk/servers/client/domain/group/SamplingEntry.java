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
    private Integer cpu;
    private Float cpuPercent;
    private Integer memoryMB;
    private Float memoryPercent;
    @JsonProperty(value = "networkReceivedKBps")
    private Float networkReceivedKBps;
    @JsonProperty(value = "networkTransmittedKBps")
    private Float networkTransmittedKBps;
    private Integer diskUsageTotalCapacityMB;
    private List<DiskUsage> diskUsage;
    private List<GuestUsage> guestDiskUsage;


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

    public Float getCpuPercent() {
        return cpuPercent;
    }

    public void setCpuPercent(Float cpuPercent) {
        this.cpuPercent = cpuPercent;
    }

    public Integer getMemoryMB() {
        return memoryMB;
    }

    public void setMemoryMB(Integer memoryMB) {
        this.memoryMB = memoryMB;
    }

    public Float getMemoryPercent() {
        return memoryPercent;
    }

    public void setMemoryPercent(Float memoryPercent) {
        this.memoryPercent = memoryPercent;
    }

    public Float getNetworkReceivedKbps() {
        return networkReceivedKBps;
    }

    public void setNetworkReceivedKbps(Float networkReceivedKBps) {
        this.networkReceivedKBps = networkReceivedKBps;
    }

    public Float getNetworkTransmittedKbps() {
        return networkTransmittedKBps;
    }

    public void setNetworkTransmittedKbps(Float networkTransmittedKbps) {
        this.networkTransmittedKBps = networkTransmittedKbps;
    }

    public Integer getDiskUsageTotalCapacityMB() {
        return diskUsageTotalCapacityMB;
    }

    public void setDiskUsageTotalCapacityMB(Integer diskUsageTotalCapacityMB) {
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

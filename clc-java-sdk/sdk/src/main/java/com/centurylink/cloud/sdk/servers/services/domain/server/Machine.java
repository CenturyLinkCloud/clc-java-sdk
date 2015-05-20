package com.centurylink.cloud.sdk.servers.services.domain.server;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ilya.drabenia
 */
public class Machine {
    private Integer cpuCount;
    private Integer ram;
    private List<DiskConfig> disks = new ArrayList<>();

    public Integer getCpuCount() {
        return cpuCount;
    }

    public void setCpuCount(Integer cpuCount) {
        this.cpuCount = cpuCount;
    }

    public Machine cpuCount(Integer cpuCount) {
        setCpuCount(cpuCount);
        return this;
    }

    public Integer getRam() {
        return ram;
    }

    public void setRam(Integer ram) {
        this.ram = ram;
    }

    public Machine ram(Integer ram) {
        setRam(ram);
        return this;
    }

    public List<DiskConfig> getDisks() {
        return disks;
    }

    public void setDisks(List<DiskConfig> disks) {
        this.disks = disks;
    }

    public Machine disk(DiskConfig diskConfig) {
        disks.add(diskConfig);
        return this;
    }
}

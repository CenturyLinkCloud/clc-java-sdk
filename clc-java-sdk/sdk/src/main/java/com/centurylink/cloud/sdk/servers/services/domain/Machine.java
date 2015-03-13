package com.centurylink.cloud.sdk.servers.services.domain;

/**
 * @author ilya.drabenia
 */
public class Machine {
    private Integer cpuCount;
    private Integer ram;

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
}

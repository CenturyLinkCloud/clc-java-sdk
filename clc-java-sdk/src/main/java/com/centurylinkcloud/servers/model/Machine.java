package com.centurylinkcloud.servers.model;

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

    public Integer getRam() {
        return ram;
    }

    public void setRam(Integer ram) {
        this.ram = ram;
    }
}

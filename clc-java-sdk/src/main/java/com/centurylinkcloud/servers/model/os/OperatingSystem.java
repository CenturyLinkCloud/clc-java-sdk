package com.centurylinkcloud.servers.model.os;

/**
 * @author ilya.drabenia
 */
public class OperatingSystem {
    private OsType type;
    private CpuArchitecture architecture;
    private String version;

    public OperatingSystem type(OsType type) {
        this.type = type;
        return this;
    }

    public OsType getType() {
        return type;
    }

    public OperatingSystem architecture(CpuArchitecture architecture) {
        this.architecture = architecture;
        return this;
    }

    public CpuArchitecture getArchitecture() {
        return architecture;
    }

    public OperatingSystem version(String version) {
        this.version = version;
        return this;
    }

    public String getVersion() {
        return version;
    }
}

package com.centurylinkcloud.servers.domain.os;

/**
 * @author ilya.drabenia
 */
public class OperatingSystem {
    private String type;
    private CpuArchitecture architecture;
    private String edition;
    private String version;

    public OperatingSystem type(String type) {
        this.type = type;
        return this;
    }

    public OperatingSystem type(OsType type) {
        this.type = type.getCode();
        return this;
    }

    public String getType() {
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

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public OperatingSystem edition(String edition) {
        setEdition(edition);
        return this;
    }
}

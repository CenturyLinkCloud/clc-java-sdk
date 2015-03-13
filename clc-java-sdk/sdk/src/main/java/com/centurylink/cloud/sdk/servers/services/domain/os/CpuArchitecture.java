package com.centurylink.cloud.sdk.servers.services.domain.os;

/**
 * @author ilya.drabenia
 */
public enum CpuArchitecture {
    i386("32Bit"),
    x86_64("64Bit");

    private final String code;

    private CpuArchitecture(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

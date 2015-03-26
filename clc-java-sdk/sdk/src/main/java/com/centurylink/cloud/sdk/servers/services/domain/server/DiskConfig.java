package com.centurylink.cloud.sdk.servers.services.domain.server;

/**
 * @author Ilya Drabenia
 */
public class DiskConfig {
    private DiskType diskType;
    private Integer size;
    private String path;

    public DiskConfig type(DiskType type) {
        diskType = type;
        return this;
    }

    public DiskConfig size(Integer size) {
        this.size = size;
        return this;
    }

    public DiskConfig path(String path) {
        this.path = path;
        return this;
    }
}

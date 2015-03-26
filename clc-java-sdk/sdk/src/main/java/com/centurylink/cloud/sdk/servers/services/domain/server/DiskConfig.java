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

    public DiskType getDiskType() {
        return diskType;
    }

    public void setDiskType(DiskType diskType) {
        this.diskType = diskType;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

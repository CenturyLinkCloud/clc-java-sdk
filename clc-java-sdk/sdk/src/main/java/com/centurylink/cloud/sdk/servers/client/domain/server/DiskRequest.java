package com.centurylink.cloud.sdk.servers.client.domain.server;

/**
 * @author Ilya Drabenia
 */
public class DiskRequest {

    private String diskId;
    private String path;
    private String type;
    private Integer sizeGB;

    public String getDiskId() {
        return diskId;
    }

    public void setDiskId(String diskId) {
        this.diskId = diskId;
    }

    public DiskRequest diskId(String diskId) {
        setDiskId(diskId);
        return this;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public DiskRequest path(String path) {
        setPath(path);
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DiskRequest type(String type) {
        setType(type);
        return this;
    }

    public Integer getSizeGB() {
        return sizeGB;
    }

    public void setSizeGB(Integer sizeGB) {
        this.sizeGB = sizeGB;
    }

    public DiskRequest sizeGB(Integer sizeGB) {
        setSizeGB(sizeGB);
        return this;
    }
}

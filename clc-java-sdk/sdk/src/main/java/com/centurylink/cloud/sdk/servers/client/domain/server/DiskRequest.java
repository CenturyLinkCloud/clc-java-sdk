package com.centurylink.cloud.sdk.servers.client.domain.server;

/**
 * @author Ilya Drabenia
 */
public class DiskRequest {
    private String path;
    private String type;
    private Integer sizeGB;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSizeGB() {
        return sizeGB;
    }

    public void setSizeGB(Integer sizeGB) {
        this.sizeGB = sizeGB;
    }
}

package com.centurylink.cloud.sdk.servers.client.domain.server;

public class RestoreServerRequest {

    private String targetGroupId;

    public String getTargetGroupId() {
        return targetGroupId;
    }

    public void setTargetGroupId(String targetGroupId) {
        this.targetGroupId = targetGroupId;
    }

    public RestoreServerRequest targetGroupId(String targetGroupId) {
        setTargetGroupId(targetGroupId);
        return this;
    }
}
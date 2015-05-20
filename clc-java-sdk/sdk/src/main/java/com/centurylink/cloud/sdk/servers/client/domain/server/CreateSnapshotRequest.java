package com.centurylink.cloud.sdk.servers.client.domain.server;

import java.util.List;

public class CreateSnapshotRequest {

    private Integer snapshotExpirationDays;
    private List<String> serverIds;

    private final Integer defaultExpiration = 10;

    public Integer getSnapshotExpirationDays() {
        return snapshotExpirationDays;
    }

    public void setSnapshotExpirationDays(Integer snapshotExpirationDays) {
        if (snapshotExpirationDays == null || snapshotExpirationDays <= 0 || snapshotExpirationDays > defaultExpiration) {
            snapshotExpirationDays = defaultExpiration;
        }

        this.snapshotExpirationDays = snapshotExpirationDays;
    }

    public CreateSnapshotRequest snapshotExpirationDays(Integer snapshotExpirationDays) {
        setSnapshotExpirationDays(snapshotExpirationDays);
        return this;
    }

    public List<String> getServerIds() {
        return serverIds;
    }

    public void setServerIds(List<String> serverIds) {
        this.serverIds = serverIds;
    }

    public CreateSnapshotRequest serverIds(List<String> serverIds) {
        setServerIds(serverIds);
        return this;
    }
}
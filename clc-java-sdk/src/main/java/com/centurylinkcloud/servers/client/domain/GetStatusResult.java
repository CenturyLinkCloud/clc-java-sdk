package com.centurylinkcloud.servers.client.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author ilya.drabenia
 */
public class GetStatusResult {
    private final String status;

    public GetStatusResult(@JsonProperty("status") String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}

package com.centurylink.cloud.sdk.servers.client.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author ilya.drabenia
 */
public class GetStatusResponse {
    private final String status;

    public GetStatusResponse(@JsonProperty("status") String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}

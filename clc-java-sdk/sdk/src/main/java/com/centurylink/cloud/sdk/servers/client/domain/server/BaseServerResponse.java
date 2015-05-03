package com.centurylink.cloud.sdk.servers.client.domain.server;

import com.centurylink.cloud.sdk.core.client.ClcClientException;
import com.centurylink.cloud.sdk.core.client.domain.Link;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BaseServerResponse {
    private final String server;
    private final Boolean isQueued;
    private final List<Link> links;
    private final String errorMessage;

    public BaseServerResponse(
        @JsonProperty("server") String server,
        @JsonProperty("isQueued") Boolean queued,
        @JsonProperty("links") List<Link> links,
        @JsonProperty("errorMessage") String errorMessage) {
        this.server = server;
        this.isQueued = queued;
        if (isQueued != null && !isQueued) {
            throw new ClcClientException("The job was not queued: " + errorMessage);
        }
        this.links = links;
        this.errorMessage = errorMessage;
    }

    public String getServer() {
        return server;
    }

    public Boolean getQueued() {
        return isQueued;
    }

    public List<Link> getLinks() {
        return links;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String findServerUuid() {
        for (Link curLink : links) {
            if (curLink.getRel().equals("self")) {
                return curLink.getId();
            }
        }

        return null;
    }

    public String findStatusId() {
        if (links != null) {
            for (Link curLink : links) {
                if (curLink.getRel().equals("status")) {
                    return curLink.getId();
                }
            }
        }

        return null;
    }
}

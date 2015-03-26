package com.centurylink.cloud.sdk.servers.client.domain.server;

import com.centurylink.cloud.sdk.core.client.domain.Link;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class CreateServerResponse {
    private final String server;
    private final Boolean isQueued;
    private final List<Link> links;

    public CreateServerResponse(
            @JsonProperty("server") String server,
            @JsonProperty("isQueued") Boolean queued,
            @JsonProperty("links") List<Link> links) {
        this.server = server;
        isQueued = queued;
        this.links = links;
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

    public String findServerUuid() {
        for (Link curLink : links) {
            if (curLink.getRel().equals("self")) {
                return curLink.getId();
            }
        }

        return null;
    }

    public String findStatusId() {
        for (Link curLink : links) {
            if (curLink.getRel().equals("status")) {
                return curLink.getId();
            }
        }

        return null;
    }
}

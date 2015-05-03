package com.centurylink.cloud.sdk.servers.client.domain.server;

import com.centurylink.cloud.sdk.base.client.domain.Link;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * @author Aliaksandr Krasitski
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Snapshot {
    private String name;
    private List<Link> links;

    private static final String REL = "self";
    private static final String SLASH = "/";

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        String[] splitedHref = splitHref();

        return splitedHref[splitedHref.length-1];
    }

    @JsonIgnore
    public String getServerId() {
        String[] splitedHref = splitHref();

        return splitedHref[splitedHref.length-1-2];
    }

    /**
     * Returns href of snapshot in format
     * "/v2/servers/{accountAlias}/{serverId}/snapshots/{snapshotId}"
     * @return href
     */
    @JsonIgnore
    private String getHref() {
        return this.getLinks().stream()
            .filter(link -> link.getRel().equals(REL))
            .map(Link::getHref)
            .findFirst()
            .get();
    }

    @JsonIgnore
    private String[] splitHref() {
        return getHref().split(SLASH);
    }
}

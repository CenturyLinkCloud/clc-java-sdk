/*
 * (c) 2015 CenturyLink Cloud. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.centurylink.cloud.sdk.servers.client.domain.server;

import com.centurylink.cloud.sdk.core.client.domain.Link;
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

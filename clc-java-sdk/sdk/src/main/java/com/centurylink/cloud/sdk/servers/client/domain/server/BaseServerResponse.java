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
        this.links = links;
        this.errorMessage = errorMessage;
    }

    public String getServer() {
        return server;
    }

    public BaseServerResponse server(String server) {
        return new BaseServerResponse(server, isQueued, links, errorMessage);
    }

    public Boolean getQueued() {
        return isQueued;
    }

    public BaseServerResponse queued(Boolean isQueued) {
        return new BaseServerResponse(server, isQueued, links, errorMessage);
    }

    public List<Link> getLinks() {
        return links;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public BaseServerResponse errorMessage(String errorMessage) {
        return new BaseServerResponse(server, isQueued, links, errorMessage);
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

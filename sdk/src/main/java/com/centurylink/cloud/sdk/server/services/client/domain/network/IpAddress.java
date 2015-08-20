/*
 * (c) 2015 CenturyLink. All Rights Reserved.
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

package com.centurylink.cloud.sdk.server.services.client.domain.network;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "address",
    "claimed",
    "server",
    "type",
    "primary"
})
public class IpAddress {

    @JsonProperty("address")
    private String address;
    @JsonProperty("claimed")
    private Boolean claimed;
    @JsonProperty("server")
    private String server;
    @JsonProperty("type")
    private String type;
    @JsonProperty("primary")
    private Boolean primary;

    /**
     *
     * @return
     * The address
     */
    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    /**
     *
     * @param address
     * The address
     */
    @JsonProperty("address")
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     *
     * @return
     * The claimed
     */
    @JsonProperty("claimed")
    public Boolean getClaimed() {
        return claimed;
    }

    /**
     *
     * @param claimed
     * The claimed
     */
    @JsonProperty("claimed")
    public void setClaimed(Boolean claimed) {
        this.claimed = claimed;
    }

    /**
     *
     * @return
     * The server
     */
    @JsonProperty("server")
    public String getServer() {
        return server;
    }

    /**
     *
     * @param server
     * The server
     */
    @JsonProperty("server")
    public void setServer(String server) {
        this.server = server;
    }

    /**
     *
     * @return
     * The type
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     * The primary
     */
    @JsonProperty("primary")
    public Boolean getPrimary() {
        return primary;
    }

    /**
     *
     * @param primary
     * The primary
     */
    @JsonProperty("primary")
    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }
}

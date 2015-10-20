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

import com.centurylink.cloud.sdk.core.client.domain.Link;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "id",
    "cidr",
    "description",
    "gateway",
    "name",
    "netmask",
    "type",
    "vlan",
    "ipAddresses",
    "links"
})
public class NetworkMetadata {

    @JsonProperty("id")
    private String id;
    @JsonProperty("cidr")
    private String cidr;
    @JsonProperty("description")
    private String description;
    @JsonProperty("gateway")
    private String gateway;
    @JsonProperty("name")
    private String name;
    @JsonProperty("netmask")
    private String netmask;
    @JsonProperty("type")
    private String type;
    @JsonProperty("vlan")
    private Integer vlan;
    @JsonProperty("ipAddresses")
    private List<IpAddress> ipAddresses = new ArrayList<>();
    @JsonProperty("links")
    private List<Link> links = new ArrayList<>();

    private String dataCenterId;

    /**
     *
     * @return
     * The id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The cidr
     */
    @JsonProperty("cidr")
    public String getCidr() {
        return cidr;
    }

    /**
     *
     * @param cidr
     * The cidr
     */
    @JsonProperty("cidr")
    public void setCidr(String cidr) {
        this.cidr = cidr;
    }

    /**
     *
     * @return
     * The description
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     * The gateway
     */
    @JsonProperty("gateway")
    public String getGateway() {
        return gateway;
    }

    /**
     *
     * @param gateway
     * The gateway
     */
    @JsonProperty("gateway")
    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    /**
     *
     * @return
     * The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The netmask
     */
    @JsonProperty("netmask")
    public String getNetmask() {
        return netmask;
    }

    /**
     *
     * @param netmask
     * The netmask
     */
    @JsonProperty("netmask")
    public void setNetmask(String netmask) {
        this.netmask = netmask;
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
     * The vlan
     */
    @JsonProperty("vlan")
    public Integer getVlan() {
        return vlan;
    }

    /**
     *
     * @param vlan
     * The vlan
     */
    @JsonProperty("vlan")
    public void setVlan(Integer vlan) {
        this.vlan = vlan;
    }

    /**
     *
     * @return
     * The ipAddresses
     */
    @JsonProperty("ipAddresses")
    public List<IpAddress> getIpAddresses() {
        return ipAddresses;
    }

    /**
     *
     * @param ipAddresses
     * The ipAddresses
     */
    @JsonProperty("ipAddresses")
    public void setIpAddresses(List<IpAddress> ipAddresses) {
        this.ipAddresses = ipAddresses;
    }

    /**
     *
     * @return
     * The links
     */
    @JsonProperty("links")
    public List<Link> getLinks() {
        return links;
    }

    /**
     *
     * @param links
     * The links
     */
    @JsonProperty("links")
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getDataCenterId() {
        return dataCenterId;
    }

    public void setDataCenterId(String dataCenterId) {
        this.dataCenterId = dataCenterId;
    }
}

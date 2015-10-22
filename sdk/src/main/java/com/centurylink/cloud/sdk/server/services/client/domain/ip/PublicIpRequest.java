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

package com.centurylink.cloud.sdk.server.services.client.domain.ip;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author aliaksandr.krasitski
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "internalIPAddress",
    "ports",
    "sourceRestrictions"
})
public class PublicIpRequest {
    private String internalIPAddress;
    private List<PortConfig> ports;
    private List<SourceRestriction> sourceRestrictions;

    public String getInternalIPAddress() {
        return internalIPAddress;
    }

    public void setInternalIPAddress(String internalIPAddress) {
        this.internalIPAddress = internalIPAddress;
    }

    public PublicIpRequest internalIPAddress(String internalIPAddress) {
        setInternalIPAddress(internalIPAddress);
        return this;
    }

    public List<PortConfig> getPorts() {
        return ports;
    }

    public void setPorts(List<PortConfig> ports) {
        this.ports = ports;
    }

    public PublicIpRequest ports(List<PortConfig> ports) {
        setPorts(ports);
        return this;
    }

    public List<SourceRestriction> getSourceRestrictions() {
        return sourceRestrictions;
    }

    /**
     * Set source restrictions in CIDR(Classless Inter-Domain Routing) format
     * @param sourceRestrictions the list of {@link SourceRestriction}
     */
    public void setSourceRestrictions(List<SourceRestriction> sourceRestrictions) {
        this.sourceRestrictions = sourceRestrictions;
    }

    /**
     * Specify List of source restrictions in CIDR(Classless Inter-Domain Routing) format
     * @param sourceRestrictions list of source restrictions
     * @return PublicIp configuration
     */
    public PublicIpRequest sourceRestrictions(List<String> sourceRestrictions) {
        List<SourceRestriction> restrictions = new ArrayList<>(sourceRestrictions.size());
        sourceRestrictions.forEach(value -> restrictions.add(new SourceRestriction(value)));
        setSourceRestrictions(restrictions);
        return this;
    }

    /**
     * Specify List of source restrictions in CIDR(Classless Inter-Domain Routing) format
     * @param sourceRestrictions array of source restrictions
     * @return PublicIp configuration
     */
    public PublicIpRequest sourceRestrictions(String... sourceRestrictions) {
        return sourceRestrictions(Arrays.asList(sourceRestrictions));
    }
}

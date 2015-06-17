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

package com.centurylink.cloud.sdk.server.services.client.domain.server;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "public",
        "internal"
})
public class IpAddress {

    @JsonProperty("internal")
    private String internal;
    @JsonProperty("public")
    private String publicIp;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The internal
     */
    @JsonProperty("internal")
    public String getInternal() {
        return internal;
    }

    /**
     *
     * @param internal
     * The internal
     */
    @JsonProperty("internal")
    public void setInternal(String internal) {
        this.internal = internal;
    }

    /**
     *
     * @return
     * The public IP
     */
    @JsonProperty("public")
    public String getPublicIp() {
        return publicIp;
    }

    /**
     *
     * @param publicIp
     * The public IP
     */
    @JsonProperty("public")
    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
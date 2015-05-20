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

package com.centurylink.cloud.sdk.servers.client.domain.server;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "id",
        "sizeGB",
        "partitionPaths"
})
public class Disk {

    @JsonProperty("id")
    private String id;
    @JsonProperty("sizeGB")
    private Integer sizeGB;
    @JsonProperty("partitionPaths")
    private List<Object> partitionPaths = new ArrayList<Object>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
     * The sizeGB
     */
    @JsonProperty("sizeGB")
    public Integer getSizeGB() {
        return sizeGB;
    }

    /**
     *
     * @param sizeGB
     * The sizeGB
     */
    @JsonProperty("sizeGB")
    public void setSizeGB(Integer sizeGB) {
        this.sizeGB = sizeGB;
    }

    /**
     *
     * @return
     * The partitionPaths
     */
    @JsonProperty("partitionPaths")
    public List<Object> getPartitionPaths() {
        return partitionPaths;
    }

    /**
     *
     * @param partitionPaths
     * The partitionPaths
     */
    @JsonProperty("partitionPaths")
    public void setPartitionPaths(List<Object> partitionPaths) {
        this.partitionPaths = partitionPaths;
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
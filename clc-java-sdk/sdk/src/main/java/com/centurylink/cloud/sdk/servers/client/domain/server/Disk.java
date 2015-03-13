package com.centurylink.cloud.sdk.servers.client.domain.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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
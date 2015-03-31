package com.centurylink.cloud.sdk.servers.client.domain.server;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "sizeGB",
        "path"
})
public class Partition {

    @JsonProperty("sizeGB")
    private Double sizeGB;
    @JsonProperty("path")
    private String path;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The sizeGB
     */
    @JsonProperty("sizeGB")
    public Double getSizeGB() {
        return sizeGB;
    }

    /**
     *
     * @param sizeGB
     * The sizeGB
     */
    @JsonProperty("sizeGB")
    public void setSizeGB(Double sizeGB) {
        this.sizeGB = sizeGB;
    }

    /**
     *
     * @return
     * The path
     */
    @JsonProperty("path")
    public String getPath() {
        return path;
    }

    /**
     *
     * @param path
     * The path
     */
    @JsonProperty("path")
    public void setPath(String path) {
        this.path = path;
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
package com.centurylink.cloud.sdk.servers.client.domain.server;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "internal"
})
public class IpAddress {

    @JsonProperty("internal")
    private String internal;
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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
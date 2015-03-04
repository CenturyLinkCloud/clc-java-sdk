package com.centurylinkcloud.servers.client.domain.deployment.capabilities;

import java.util.HashMap;
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
        "description",
        "labProductCode",
        "premiumProductCode",
        "type"
})
public class ImportableOSType {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("description")
    private String description;
    @JsonProperty("labProductCode")
    private String labProductCode;
    @JsonProperty("premiumProductCode")
    private String premiumProductCode;
    @JsonProperty("type")
    private String type;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The id
     */
    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
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
     * The labProductCode
     */
    @JsonProperty("labProductCode")
    public String getLabProductCode() {
        return labProductCode;
    }

    /**
     *
     * @param labProductCode
     * The labProductCode
     */
    @JsonProperty("labProductCode")
    public void setLabProductCode(String labProductCode) {
        this.labProductCode = labProductCode;
    }

    /**
     *
     * @return
     * The premiumProductCode
     */
    @JsonProperty("premiumProductCode")
    public String getPremiumProductCode() {
        return premiumProductCode;
    }

    /**
     *
     * @param premiumProductCode
     * The premiumProductCode
     */
    @JsonProperty("premiumProductCode")
    public void setPremiumProductCode(String premiumProductCode) {
        this.premiumProductCode = premiumProductCode;
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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
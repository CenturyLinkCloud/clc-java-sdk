package com.centurylink.cloud.sdk.servers.client.domain.server.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

import com.centurylink.cloud.sdk.servers.client.domain.Link;
import com.centurylink.cloud.sdk.servers.client.domain.ChangeInfo;
import com.centurylink.cloud.sdk.servers.client.domain.server.Details;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.IdServerRef;
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
        "name",
        "description",
        "groupId",
        "isTemplate",
        "locationId",
        "osType",
        "os",
        "status",
        "details",
        "type",
        "storageType",
        "changeInfo",
        "links"
})
public class ServerMetadata {

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("groupId")
    private String groupId;
    @JsonProperty("isTemplate")
    private Boolean isTemplate;
    @JsonProperty("locationId")
    private String locationId;
    @JsonProperty("osType")
    private String osType;
    @JsonProperty("os")
    private String os;
    @JsonProperty("status")
    private String status;
    @JsonProperty("details")
    private Details details;
    @JsonProperty("type")
    private String type;
    @JsonProperty("storageType")
    private String storageType;
    @JsonProperty("changeInfo")
    private ChangeInfo changeInfo;
    @JsonProperty("links")
    private List<Link> links = new ArrayList<Link>();
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
     * The groupId
     */
    @JsonProperty("groupId")
    public String getGroupId() {
        return groupId;
    }

    /**
     *
     * @param groupId
     * The groupId
     */
    @JsonProperty("groupId")
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     *
     * @return
     * The isTemplate
     */
    @JsonProperty("isTemplate")
    public Boolean getIsTemplate() {
        return isTemplate;
    }

    /**
     *
     * @param isTemplate
     * The isTemplate
     */
    @JsonProperty("isTemplate")
    public void setIsTemplate(Boolean isTemplate) {
        this.isTemplate = isTemplate;
    }

    /**
     *
     * @return
     * The locationId
     */
    @JsonProperty("locationId")
    public String getLocationId() {
        return locationId;
    }

    /**
     *
     * @param locationId
     * The locationId
     */
    @JsonProperty("locationId")
    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    /**
     *
     * @return
     * The osType
     */
    @JsonProperty("osType")
    public String getOsType() {
        return osType;
    }

    /**
     *
     * @param osType
     * The osType
     */
    @JsonProperty("osType")
    public void setOsType(String osType) {
        this.osType = osType;
    }

    /**
     *
     * @return
     * The os
     */
    @JsonProperty("os")
    public String getOs() {
        return os;
    }

    /**
     *
     * @param os
     * The os
     */
    @JsonProperty("os")
    public void setOs(String os) {
        this.os = os;
    }

    /**
     *
     * @return
     * The status
     */
    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The details
     */
    @JsonProperty("details")
    public Details getDetails() {
        return details;
    }

    /**
     *
     * @param details
     * The details
     */
    @JsonProperty("details")
    public void setDetails(Details details) {
        this.details = details;
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
     * The storageType
     */
    @JsonProperty("storageType")
    public String getStorageType() {
        return storageType;
    }

    /**
     *
     * @param storageType
     * The storageType
     */
    @JsonProperty("storageType")
    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    /**
     *
     * @return
     * The changeInfo
     */
    @JsonProperty("changeInfo")
    public ChangeInfo getChangeInfo() {
        return changeInfo;
    }

    /**
     *
     * @param changeInfo
     * The changeInfo
     */
    @JsonProperty("changeInfo")
    public void setChangeInfo(ChangeInfo changeInfo) {
        this.changeInfo = changeInfo;
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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public IdServerRef asRefById() {
        return new IdServerRef(this.getId());
    }

}
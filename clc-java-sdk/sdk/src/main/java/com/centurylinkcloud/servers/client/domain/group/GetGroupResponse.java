package com.centurylinkcloud.servers.client.domain.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

import com.centurylinkcloud.servers.client.domain.ChangeInfo;
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
        "locationId",
        "type",
        "status",
        "serversCount",
        "groups",
        "links",
        "changeInfo",
        "customFields"
})
public class GetGroupResponse extends Group {

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("locationId")
    private String locationId;
    @JsonProperty("type")
    private String type;
    @JsonProperty("status")
    private String status;
    @JsonProperty("serversCount")
    private Integer serversCount;
    @JsonProperty("groups")
    private List<Group> groups = new ArrayList<Group>();
    @JsonProperty("links")
    private List<Link> links = new ArrayList<Link>();
    @JsonProperty("changeInfo")
    private ChangeInfo changeInfo;
    @JsonProperty("customFields")
    private List<Object> customFields = new ArrayList<Object>();
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
     * The serversCount
     */
    @JsonProperty("serversCount")
    public Integer getServersCount() {
        return serversCount;
    }

    /**
     *
     * @param serversCount
     * The serversCount
     */
    @JsonProperty("serversCount")
    public void setServersCount(Integer serversCount) {
        this.serversCount = serversCount;
    }

    /**
     *
     * @return
     * The groups
     */
    @JsonProperty("groups")
    public List<Group> getGroups() {
        return groups;
    }

    /**
     *
     * @param groups
     * The groups
     */
    @JsonProperty("groups")
    public void setGroups(List<Group> groups) {
        this.groups = groups;
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
     * The customFields
     */
    @JsonProperty("customFields")
    public List<Object> getCustomFields() {
        return customFields;
    }

    /**
     *
     * @param customFields
     * The customFields
     */
    @JsonProperty("customFields")
    public void setCustomFields(List<Object> customFields) {
        this.customFields = customFields;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Group findGroupByName(String name) {
        return findGroupByName(groups, name);
    }

    public Group findGroupByName(List<Group> groups, String name) {
        for (Group curGroup : groups) {
            if (curGroup.getName().equals(name)) {
                return curGroup;
            } else if (curGroup.getGroups().size() > 0
                    && findGroupByName(curGroup.getGroups(), name) != null) {
                return findGroupByName(curGroup.getGroups(), name);
            }
        }

        return null;
    }

    public List<Group> getAllGroups() {
        List<Group> groups = new ArrayList<>();

        groups.add(this);

        for (Group curGroup : this.getGroups()) {
            groups.addAll(getSubGroups(curGroup));
        }

        return groups;
    }

    private List<Group> getSubGroups(final Group curGroup) {
        if (curGroup.getGroups() == null || curGroup.getGroups().isEmpty()) {
            return new ArrayList<Group>() {{ add(curGroup); }};
        }

        List<Group> result = new ArrayList<>();

        result.addAll(curGroup.getGroups());
        for (Group group : curGroup.getGroups()) {
            result.addAll(getSubGroups(group));
        }

        return result;
    }

}

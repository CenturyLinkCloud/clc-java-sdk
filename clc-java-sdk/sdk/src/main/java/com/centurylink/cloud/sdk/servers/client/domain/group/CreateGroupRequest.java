package com.centurylink.cloud.sdk.servers.client.domain.group;

import com.centurylink.cloud.sdk.core.commons.client.domain.CustomField;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * @author Aliaksandr Krasitski
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateGroupRequest {
    private String name;
    private String description;
    private String parentGroupId;
    private List<CustomField> customFields;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CreateGroupRequest name(String name) {
        setName(name);
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CreateGroupRequest description(String description) {
        setDescription(description);
        return this;
    }

    public String getParentGroupId() {
        return parentGroupId;
    }

    public void setParentGroupId(String parentGroupId) {
        this.parentGroupId = parentGroupId;
    }

    public CreateGroupRequest parentGroupId(String parentGroupId) {
        setParentGroupId(parentGroupId);
        return this;
    }

    public List<CustomField> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<CustomField> customFields) {
        this.customFields = customFields;
    }

    public CreateGroupRequest customFields(List<CustomField> customFields) {
        setCustomFields(customFields);
        return this;
    }
}

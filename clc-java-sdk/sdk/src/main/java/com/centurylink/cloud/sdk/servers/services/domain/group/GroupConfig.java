package com.centurylink.cloud.sdk.servers.services.domain.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Aliaksandr Krasitski
 */
public class GroupConfig {
    private String name;
    private String description;
    private String parentGroupId;
    private Map<String, String> customFields = new HashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GroupConfig name(String name) {
        setName(name);
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GroupConfig description(String description) {
        setDescription(description);
        return this;
    }

    public String getParentGroupId() {
        return parentGroupId;
    }

    public void setParentGroupId(String parentGroupId) {
        this.parentGroupId = parentGroupId;
    }

    public GroupConfig parentGroupId(String parentGroupId) {
        setParentGroupId(parentGroupId);
        return this;
    }

    public Map<String, String> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(Map<String, String> customFields) {
        this.customFields = customFields;
    }

    public GroupConfig customFields(Map<String, String> customFields) {
        setCustomFields(customFields);
        return this;
    }
}

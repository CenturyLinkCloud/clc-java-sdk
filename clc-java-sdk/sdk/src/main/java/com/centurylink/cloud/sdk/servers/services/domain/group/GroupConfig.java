package com.centurylink.cloud.sdk.servers.services.domain.group;

import com.centurylink.cloud.sdk.core.commons.client.domain.CustomField;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * @author Aliaksandr Krasitski
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupConfig {
    private String name;
    private String description;
    private Group parentGroup;
    private List<CustomField> customFields;

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

    public Group getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(Group parentGroup) {
        this.parentGroup = parentGroup;
    }

    public GroupConfig parentGroup(Group parentGroup) {
        setParentGroup(parentGroup);
        return this;
    }

    public List<CustomField> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<CustomField> customFields) {
        this.customFields = customFields;
    }

    public GroupConfig customFields(List<CustomField> customFields) {
        setCustomFields(customFields);
        return this;
    }
}

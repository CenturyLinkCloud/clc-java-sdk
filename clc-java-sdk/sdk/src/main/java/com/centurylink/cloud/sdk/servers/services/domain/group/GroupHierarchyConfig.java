package com.centurylink.cloud.sdk.servers.services.domain.group;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.asList;

/**
 * @author Aliaksandr Krasitski
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupHierarchyConfig {
    private String name;
    private String description;
    private List<GroupHierarchyConfig> subgroups = new ArrayList<>();

    public String getName() {
        return name;
    }

    public GroupHierarchyConfig name(String name) {
        this.name = name;
        return this;
    }

    public List<GroupHierarchyConfig> getSubgroups() {
        return subgroups;
    }

    public GroupHierarchyConfig subgroups(GroupHierarchyConfig... subgroups) {
        checkNotNull(subgroups, "List of subgroups must be not a null");
        this.subgroups.addAll(asList(subgroups));
        return this;
    }

    public String getDescription() {
        return description;
    }

    public GroupHierarchyConfig description(String description) {
        this.description = description;
        return this;
    }

    public static GroupHierarchyConfig group(String name) {
        return new GroupHierarchyConfig().name(name);
    }
}

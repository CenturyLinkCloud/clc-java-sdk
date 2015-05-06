package com.centurylink.cloud.sdk.servers.services.domain.group;

import com.centurylink.cloud.sdk.servers.services.domain.server.CreateServerConfig;
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
    private List<CreateServerConfig> subitems = new ArrayList<>();

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

    public List<CreateServerConfig> getSubitems() {
        return subitems;
    }

    public GroupHierarchyConfig subitems(CreateServerConfig... subitems) {
        checkNotNull(subitems, "List of server configs must be not a null");
        this.subitems.addAll(asList(subitems));
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

    public List<CreateServerConfig> getServerConfigs() {
        List<CreateServerConfig> serverConfigs = new ArrayList<>();
        collectConfigs(this, serverConfigs);
        return serverConfigs;
    }

    private void collectConfigs(GroupHierarchyConfig config, List<CreateServerConfig> serverConfigs) {
        config.getSubitems().stream().forEach(serverConfig -> {
            for (int i=0;i<serverConfig.getCount();i++) {
                serverConfigs.add(serverConfig);
            }
        });
        config.getSubgroups().stream()
            .forEach(subgroupConfig -> collectConfigs(subgroupConfig, serverConfigs));
    }
}

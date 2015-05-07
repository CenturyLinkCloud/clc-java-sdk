package com.centurylink.cloud.sdk.servers.services.domain.group;

import com.centurylink.cloud.sdk.servers.services.domain.server.CreateServerConfig;
import com.centurylink.cloud.sdk.servers.services.domain.server.ServerConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.asList;

/**
 * Hierarchy configuration with groups and servers.
 * Should contain {@link GroupHierarchyConfig} {@code subitems}.
 *
 * @author Aliaksandr Krasitski
 */
public class GroupHierarchyConfig implements InfrastructureItem {
    private String name;
    private String description;
    private List<InfrastructureItem> subitems = new ArrayList<>();

    /**
     * Returns name of group
     * @return group name
     */
    public String getName() {
        return name;
    }

    /**
     * Set group name
     * @param name group name
     * @return current {@link GroupHierarchyConfig} instance
     */
    public GroupHierarchyConfig name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Returns items, specified for current group
     * @return sub items
     */
    public List<InfrastructureItem> getSubitems() {
        return subitems;
    }

    /**
     * Set sub items
     * @param subitems items, that specified for group
     * @return current {@link GroupHierarchyConfig} instance
     */
    public GroupHierarchyConfig subitems(InfrastructureItem... subitems) {
        checkNotNull(subitems, "List of server configs must be not a null");
        this.subitems.addAll(asList(subitems));
        return this;
    }

    /**
     * Returns group description
     * @return group description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set group description
     * @param description group description
     * @return current {@link GroupHierarchyConfig} instance
     */
    public GroupHierarchyConfig description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Constructor for group
     * @param name group name
     * @return new {@link GroupHierarchyConfig} instance with group name {@code name}
     */
    public static GroupHierarchyConfig group(String name) {
        return new GroupHierarchyConfig().name(name);
    }

    /**
     * Returns all configs for creating servers
     * @return all {@link CreateServerConfig} configs
     */
    public List<CreateServerConfig> getServerConfigs() {
        List<CreateServerConfig> serverConfigs = new ArrayList<>();
        collectConfigs(this, serverConfigs);
        return serverConfigs;
    }

    /**
     * Collect server configs in current group
     * @param hierarchyConfig group hierarchy config
     * @param serverConfigs list with server configs
     */
    private void collectConfigs(GroupHierarchyConfig hierarchyConfig, List<CreateServerConfig> serverConfigs) {
        hierarchyConfig.getSubitems().stream().forEach(config -> {
            if (config instanceof ServerConfig) {
                serverConfigs.addAll(Arrays.asList(((ServerConfig)config).getServerConfig()));
            } else {
                collectConfigs((GroupHierarchyConfig) config, serverConfigs);
            }
        });
    }
}

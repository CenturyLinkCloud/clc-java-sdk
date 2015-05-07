package com.centurylink.cloud.sdk.servers.services.domain.group;

import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.servers.services.domain.server.CreateServerConfig;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.asList;

/**
 * @author Aliaksandr Krasitski
 */
public class GroupHierarchyConfig implements ISubItemConfig{
    private String name;
    private String description;
    private List<ISubItemConfig> subitems = new ArrayList<>();
    private List<DataCenter> dataCenters = new ArrayList<>();

    public String getName() {
        return name;
    }

    public GroupHierarchyConfig name(String name) {
        this.name = name;
        return this;
    }

    public List<ISubItemConfig> getSubitems() {
        return subitems;
    }

    public GroupHierarchyConfig subitems(ISubItemConfig... subitems) {
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

    public List<DataCenter> getDataCenters() {
        return dataCenters;
    }

    public GroupHierarchyConfig datacenter(DataCenter... datacenters) {
        checkNotNull(datacenters, "List of dataCenters must be not a null");
        this.dataCenters.addAll(asList(datacenters));
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

    private void collectConfigs(GroupHierarchyConfig hierarchyConfig, List<CreateServerConfig> serverConfigs) {
        hierarchyConfig.getSubitems().stream().forEach(config -> {
            if (config instanceof CreateServerConfig) {
                CreateServerConfig cfg = (CreateServerConfig) config;
                for (int i = 0; i < cfg.getCount(); i++) {
                    serverConfigs.add(cfg);
                }
            } else {
                collectConfigs((GroupHierarchyConfig) config, serverConfigs);
            }
        });
    }
}

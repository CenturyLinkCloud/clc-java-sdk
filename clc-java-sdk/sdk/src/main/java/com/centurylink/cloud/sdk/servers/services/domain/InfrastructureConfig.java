package com.centurylink.cloud.sdk.servers.services.domain;

import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.servers.services.domain.group.GroupHierarchyConfig;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.asList;

/**
 * Hierarchy configuration with groups and servers for provided data centers.
 * Should contain data center {@code dataCenter}
 * and {@link com.centurylink.cloud.sdk.servers.services.domain.group.GroupHierarchyConfig}
 * {@code subitems}.
 * 
 * @author Aliaksandr Krasitski
 */
public class InfrastructureConfig {
    private List<GroupHierarchyConfig> subitems = new ArrayList<>();
    private List<DataCenter> dataCenters = new ArrayList<>();

    public static InfrastructureConfig dataCenter(DataCenter... dataCenters) {
        return
            new InfrastructureConfig()
                .dataCenters(dataCenters);
    }

    /**
     * Returns list of hierarchy
     * @return list of sub items
     */
    public List<GroupHierarchyConfig> getSubitems() {
        return subitems;
    }

    /**
     *
     * @param subitems array of {@link GroupHierarchyConfig}
     * @return current class instance
     */
    public InfrastructureConfig subitems(GroupHierarchyConfig... subitems) {
        checkNotNull(subitems, "List of server configs must be not a null");
        this.subitems.addAll(asList(subitems));
        return this;
    }

    /**
     * Returns data centers
     * @return list of data centers
     */
    public List<DataCenter> getDataCenters() {
        return dataCenters;
    }

    /**
     *
     * @param datacenters array of data centers
     * @return current class instance
     */
    public InfrastructureConfig dataCenters(DataCenter... datacenters) {
        checkNotNull(datacenters, "List of dataCenters must be not a null");
        this.dataCenters.addAll(asList(datacenters));
        return this;
    }
}

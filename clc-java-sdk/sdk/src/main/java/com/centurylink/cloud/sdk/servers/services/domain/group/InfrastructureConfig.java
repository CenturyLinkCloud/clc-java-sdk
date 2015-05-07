package com.centurylink.cloud.sdk.servers.services.domain.group;

import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.asList;

/**
 * @author Aliaksandr Krasitski
 */
public class InfrastructureConfig{
    private List<GroupHierarchyConfig> subitems = new ArrayList<>();
    private List<DataCenter> dataCenters = new ArrayList<>();

    public List<GroupHierarchyConfig> getSubitems() {
        return subitems;
    }

    public InfrastructureConfig subitems(GroupHierarchyConfig... subitems) {
        checkNotNull(subitems, "List of server configs must be not a null");
        this.subitems.addAll(asList(subitems));
        return this;
    }

    public List<DataCenter> getDataCenters() {
        return dataCenters;
    }

    public InfrastructureConfig datacenter(DataCenter... datacenters) {
        checkNotNull(datacenters, "List of dataCenters must be not a null");
        this.dataCenters.addAll(asList(datacenters));
        return this;
    }
}

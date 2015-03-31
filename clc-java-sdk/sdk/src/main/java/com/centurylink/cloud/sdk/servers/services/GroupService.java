package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.core.datacenters.client.DataCentersClient;
import com.centurylink.cloud.sdk.core.datacenters.services.DataCenterService;
import com.centurylink.cloud.sdk.core.datacenters.services.domain.refs.DataCenterRef;
import com.centurylink.cloud.sdk.core.exceptions.ReferenceNotSupportedException;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.group.GetGroupResponse;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupResponse;
import com.centurylink.cloud.sdk.servers.services.domain.group.Group;
import com.centurylink.cloud.sdk.servers.services.domain.group.GroupConverter;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.GroupRef;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.IdGroupRef;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.NameGroupRef;
import com.google.inject.Inject;

import java.util.List;

/**
 * @author ilya.drabenia
 */
public class GroupService {
    private final ServerClient client;
    private final GroupConverter converter;
    private final DataCentersClient dataCentersClient;
    private final DataCenterService dataCenterService;

    @Inject
    public GroupService(ServerClient client, GroupConverter converter, DataCentersClient dataCentersClient,
                        DataCenterService dataCenterService) {
        this.client = client;
        this.converter = converter;
        this.dataCentersClient = dataCentersClient;
        this.dataCenterService = dataCenterService;
    }

    public Group findByRef(GroupRef groupRef) {
        if (groupRef.is(IdGroupRef.class)) {
            GetGroupResponse response = client
                .getGroup(groupRef.as(IdGroupRef.class).getId());

            return new Group()
                .id(response.getId())
                .name(response.getName());
        } else if (groupRef.is(NameGroupRef.class)) {
            GroupResponse group = client
                .getGroup(rootGroupId(groupRef.getDataCenter()))
                .findGroupByName(groupRef.as(NameGroupRef.class).getName());

            return new Group()
                .id(group.getId())
                .name(group.getName());
        } else {
            throw new ReferenceNotSupportedException(groupRef.getClass());
        }
    }

    private String rootGroupId(DataCenterRef dataCenterRef) {
        return dataCentersClient
            .getDataCenter(
                dataCenterService.findByRef(dataCenterRef).getId()
            )
            .getGroup()
            .getId();
    }

    public List<Group> findByDataCenter(DataCenterRef dataCenter) {
        String rootGroupId = dataCentersClient
            .getDataCenter(
                dataCenterService.findByRef(dataCenter).getId()
            )
            .getGroup()
            .getId();

        GetGroupResponse result = client.getGroup(rootGroupId);

        return converter.newGroupList(
            dataCenterService.findByRef(dataCenter).getId(),
            result.getAllGroups()
        );
    }

    private GroupResponse getMatchedGroup(GetGroupResponse groups, Group group) {
        return groups
            .findGroupByName(group.getName());
    }

}

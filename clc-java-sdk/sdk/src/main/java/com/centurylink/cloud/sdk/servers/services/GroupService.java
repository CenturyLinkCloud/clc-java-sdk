package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.group.GetGroupResponse;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupResponse;
import com.centurylink.cloud.sdk.servers.services.domain.datacenter.DataCenter;
import com.centurylink.cloud.sdk.servers.services.domain.group.Group;
import com.centurylink.cloud.sdk.servers.services.domain.group.GroupConverter;
import com.google.inject.Inject;

import java.util.List;

/**
 * @author ilya.drabenia
 */
public class GroupService {
    private final ServerClient client;
    private final GroupConverter converter;
    private final DataCenterService dataCenterService;

    @Inject
    public GroupService(ServerClient client, GroupConverter converter, DataCenterService dataCenterService) {
        this.client = client;
        this.converter = converter;
        this.dataCenterService = dataCenterService;
    }

    public Group resolveId(Group group) {
        if (group.getId() != null) {
            return group;
        }

        String rootGroupId = client
            .getDataCenter(
                    dataCenterService.resolveId(group.getDataCenter()).getId()
            )
            .getGroup()
            .getId();

        GetGroupResponse groups = client.getGroup(rootGroupId);

        return new Group()
            .id(
                getMatchedGroup(groups, group).getId()
            )
            .name(
                getMatchedGroup(groups, group).getName()
            );
    }

    public List<Group> findByDataCenter(DataCenter dataCenter) {
        String rootGroupId = client
            .getDataCenter(
                dataCenterService.resolveId(dataCenter).getId()
            )
            .getGroup()
            .getId();

        GetGroupResponse result = client.getGroup(rootGroupId);

        return converter.newGroupList(
            dataCenterService.resolveId(dataCenter).getId(),
            result.getAllGroups()
        );
    }

    private GroupResponse getMatchedGroup(GetGroupResponse groups, Group group) {
        return groups
            .findGroupByName(group.getName());
    }

}

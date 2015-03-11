package com.centurylinkcloud.servers.service;

import com.centurylinkcloud.servers.client.ServerClient;
import com.centurylinkcloud.servers.client.domain.group.GetGroupResponse;
import com.centurylinkcloud.servers.domain.Group;
import com.google.inject.Inject;

import java.util.List;

/**
 * @author ilya.drabenia
 */
public class GroupService {
    private final ServerClient client;
    private final GroupConverter converter;

    @Inject
    public GroupService(ServerClient client, GroupConverter converter) {
        this.client = client;
        this.converter = converter;
    }

    public Group resolve(Group group) {
        String rootGroupId = client
                .getDataCenter(group.getDatacenter())
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

    public List<Group> findByDataCenter(String dataCenter) {
        String rootGroupId = client
                .getDataCenter(dataCenter)
                .getGroup()
                .getId();

        GetGroupResponse result = client.getGroup(rootGroupId);

        return converter.newGroupList(dataCenter, result.getAllGroups());
    }

    private com.centurylinkcloud.servers.client.domain.group.Group getMatchedGroup(GetGroupResponse groups, Group group) {
        return groups
            .findGroupByName(group.getName());
    }

}

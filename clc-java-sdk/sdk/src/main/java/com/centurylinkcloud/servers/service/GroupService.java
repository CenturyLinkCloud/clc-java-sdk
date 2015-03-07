package com.centurylinkcloud.servers.service;

import com.centurylinkcloud.servers.client.ServerClient;
import com.centurylinkcloud.servers.client.domain.group.GetGroupResponse;
import com.centurylinkcloud.servers.domain.Group;
import com.google.inject.Inject;

/**
 * @author ilya.drabenia
 */
public class GroupService {
    private final ServerClient client;

    @Inject
    public GroupService(ServerClient client) {
        this.client = client;
    }

    public Group resolve(Group group) {
        String rootGroupId = client
                .getDataCenter(group.getDatacenter())
                .getGroup()
                .getId();

        GetGroupResponse groups = client.getGroup(rootGroupId);

        return new Group()
            .id(
                getMatechedGroup(groups, group).getId()
            )
            .name(
                getMatechedGroup(groups, group).getName()
            );
    }

    private com.centurylinkcloud.servers.client.domain.group.Group getMatechedGroup(GetGroupResponse groups, Group group) {
        return groups
            .findGroupByName(group.getName());
    }

}

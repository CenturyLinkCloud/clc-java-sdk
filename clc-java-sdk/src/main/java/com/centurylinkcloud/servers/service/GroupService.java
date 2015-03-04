package com.centurylinkcloud.servers.service;

import com.centurylinkcloud.servers.client.ServerClient;
import com.centurylinkcloud.servers.client.domain.group.GetGroupResult;
import com.centurylinkcloud.servers.domain.Group;

/**
 * @author ilya.drabenia
 */
public class GroupService {
    private ServerClient client = new ServerClient();

    public Group resolve(String alias, Group group) {
        String rootGroupId = client
                .getDataCenter(alias, group.getDatacenter())
                .getGroup()
                .getId();

        GetGroupResult groups = client.getGroups(alias, rootGroupId);

        return new Group()
            .id(
                getMatechedGroup(groups, group).getId()
            )
            .name(
                getMatechedGroup(groups, group).getName()
            );
    }

    private com.centurylinkcloud.servers.client.domain.group.Group getMatechedGroup(GetGroupResult groups, Group group) {
        return groups
            .findGroupByName(group.getName());
    }

}

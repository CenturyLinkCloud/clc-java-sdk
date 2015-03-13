package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.group.GetGroupResponse;
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

    @Inject
    public GroupService(ServerClient client, GroupConverter converter) {
        this.client = client;
        this.converter = converter;
    }

    public Group resolve(Group group) {
        if (group.getId() != null) {
            return group;
        }

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

    private com.centurylink.cloud.sdk.servers.client.domain.group.Group getMatchedGroup(GetGroupResponse groups, Group group) {
        return groups
            .findGroupByName(group.getName());
    }

}

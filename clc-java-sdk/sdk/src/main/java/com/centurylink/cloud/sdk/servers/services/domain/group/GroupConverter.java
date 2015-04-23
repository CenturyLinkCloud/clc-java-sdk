package com.centurylink.cloud.sdk.servers.services.domain.group;

import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.DataCenter;
import com.centurylink.cloud.sdk.servers.client.domain.group.CreateGroupRequest;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ilya.drabenia
 */
public class GroupConverter {

    public List<Group> newGroupList(String dataCenter, List<GroupMetadata> groups) {
        return
            groups.stream()
                .map(curGroup -> newGroup(dataCenter, curGroup))
                .collect(Collectors.toList());
    }

    public Group newGroup(String dataCenter,
                          GroupMetadata group) {
        return
            new Group()
                .id(group.getId())
                .name(group.getName())
                .dataCenter(new DataCenter(dataCenter));
    }

    public CreateGroupRequest createGroupRequest(GroupConfig groupConfig, String parentGroupId) {
        return new CreateGroupRequest()
                .name(groupConfig.getName())
                .description(groupConfig.getDescription())
                .parentGroupId(parentGroupId)
                .customFields(groupConfig.getCustomFields());
    }
}

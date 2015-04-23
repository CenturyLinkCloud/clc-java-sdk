package com.centurylink.cloud.sdk.servers.services.domain.group;

import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.DataCenter;
import com.centurylink.cloud.sdk.servers.client.domain.group.*;

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

    public UpdateGroupRequest createUpdateGroupRequest(GroupConfig groupConfig, String parentGroupId) {
        UpdateGroupRequest req = new UpdateGroupRequest();

        if (groupConfig.getName() != null) {
            req.add(new SimplePatchOperation(PatchOperation.NAME, groupConfig.getName()));
        }
        if (groupConfig.getDescription() != null) {
            req.add(new SimplePatchOperation(PatchOperation.DESCRIPTION, groupConfig.getDescription()));
        }
        if (parentGroupId != null) {
            req.add(new SimplePatchOperation(PatchOperation.GROUP_PARENT_ID, parentGroupId));
        }
        if (groupConfig.getCustomFields() != null) {
            req.add(new CustomFieldPatchOperation(groupConfig.getCustomFields()));
        }
        return req;
    }
}

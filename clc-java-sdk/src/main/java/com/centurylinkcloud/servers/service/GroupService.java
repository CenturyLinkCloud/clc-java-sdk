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
        String rootGroupId = client.getDataCenter(alias, "DE1").getGroup().getId();
        GetGroupResult groups = client.getGroups(alias, rootGroupId);
        Group curGroup = new Group();
        curGroup.setId(groups.findGroupByName(group.getName()).getId());
        curGroup.setName(groups.findGroupByName(group.getName()).getName());
        return curGroup;
    }

}

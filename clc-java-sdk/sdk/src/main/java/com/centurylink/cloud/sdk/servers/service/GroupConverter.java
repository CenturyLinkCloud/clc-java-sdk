package com.centurylink.cloud.sdk.servers.service;

import com.centurylink.cloud.sdk.servers.domain.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ilya.drabenia
 */
public class GroupConverter {

    public List<Group> newGroupList(String dataCenter,
                                    List<com.centurylink.cloud.sdk.servers.client.domain.group.Group> groups) {
        List<Group> result = new ArrayList<>();

        for (com.centurylink.cloud.sdk.servers.client.domain.group.Group curGroup : groups) {
            result.add(newGroup(dataCenter, curGroup));
        }

        return result;
    }

    public Group newGroup(String dataCenter,
                          com.centurylink.cloud.sdk.servers.client.domain.group.Group group) {
        return
            new Group()
                .id(group.getId())
                .name(group.getName())
                .datacenter(dataCenter);
    }

}

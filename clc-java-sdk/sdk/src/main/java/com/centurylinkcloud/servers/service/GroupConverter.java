package com.centurylinkcloud.servers.service;

import com.centurylinkcloud.servers.domain.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ilya.drabenia
 */
public class GroupConverter {

    public List<Group> newGroupList(String dataCenter,
                                    List<com.centurylinkcloud.servers.client.domain.group.Group> groups) {
        List<Group> result = new ArrayList<>();

        for (com.centurylinkcloud.servers.client.domain.group.Group curGroup : groups) {
            result.add(newGroup(dataCenter, curGroup));
        }

        return result;
    }

    public Group newGroup(String dataCenter,
                          com.centurylinkcloud.servers.client.domain.group.Group group) {
        return
            new Group()
                .id(group.getId())
                .name(group.getName())
                .datacenter(dataCenter);
    }

}

package com.centurylink.cloud.sdk.servers.services.domain.group;

import com.centurylink.cloud.sdk.servers.client.domain.group.GroupResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ilya.drabenia
 */
public class GroupConverter {

    public List<Group> newGroupList(String dataCenter,
                                    List<GroupResponse> groups) {
        List<Group> result = new ArrayList<>();

        for (GroupResponse curGroup : groups) {
            result.add(newGroup(dataCenter, curGroup));
        }

        return result;
    }

    public Group newGroup(String dataCenter,
                          GroupResponse group) {
        return
            new Group()
                .id(group.getId())
                .name(group.getName())
                .datacenter(dataCenter);
    }

}

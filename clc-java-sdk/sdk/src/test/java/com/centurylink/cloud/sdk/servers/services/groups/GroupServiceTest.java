package com.centurylink.cloud.sdk.servers.services.groups;

import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.domain.group.GroupConfig;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.GroupByIdRef;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;

public class GroupServiceTest extends AbstractServersSdkTest {

    @Inject
    GroupService groupService;

    @Test(groups = {INTEGRATION})
    public void testFindGroupsByDataCenter() {

        List<GroupMetadata> groups = groupService.findByDataCenter(DataCenter.DE_FRANKFURT);

        assert groups.size() > 0;
    }

    @Test(groups = {INTEGRATION, LONG_RUNNING})
    public void testGroup() {
        GroupByIdRef groupRef1 = createGroup();
        GroupByIdRef groupRef2 = createGroup();

        updateGroups(groupRef1, groupRef2);
        deleteGroups(groupRef1, groupRef2);
    }

    private GroupByIdRef createGroup() {
        String newGroupName = "test group";
        String newGroupDescription = "test group description";

        GroupByIdRef newGroup = groupService.create(new GroupConfig()
            .parentGroup(Group.refByName()
                    .dataCenter(DataCenter.DE_FRANKFURT)
                    .name(Group.DEFAULT_GROUP)
            )
            .name(newGroupName)
            .description(newGroupDescription))
            .getResult().as(GroupByIdRef.class);

        GroupMetadata createdGroup = groupService.findByRef(newGroup);

        assertEquals(createdGroup.getId(), newGroup.getId());
        assertEquals(createdGroup.getName(), newGroupName);
        assertEquals(createdGroup.getDescription(), newGroupDescription);

        return newGroup;
    }

    private void updateGroups(Group groupRef1, Group groupRef2) {
        String groupName = Group.DEFAULT_GROUP + " test";
        String groupDescription = "test description";

        GroupConfig config =
            new GroupConfig()
                .name(groupName)
                .description(groupDescription);

        GroupFilter filter = toFilter(groupRef1, groupRef2);
        groupService.modify(filter, config).waitUntilComplete();

        groupService.find(filter).stream()
            .forEach(metadata -> {
                assertEquals(metadata.getDescription(), groupDescription);
                assertEquals(metadata.getName(), groupName);
            });
    }

    private void deleteGroups(Group groupRef1, Group groupRef2) {
        groupService.delete(groupRef1.asFilter().or(groupRef2.asFilter())).waitUntilComplete();
    }

    private GroupFilter toFilter(Group... groups) {
        GroupFilter filter = new GroupFilter();
        Arrays.asList(groups).stream()
            .forEach(group ->
                filter.id(groupService.findByRef(group).getId()));

        return filter;
    }

}
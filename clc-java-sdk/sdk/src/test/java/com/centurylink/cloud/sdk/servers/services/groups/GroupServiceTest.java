package com.centurylink.cloud.sdk.servers.services.groups;

import com.centurylink.cloud.sdk.core.commons.client.domain.CustomField;
import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.exceptions.ClcException;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.domain.group.GroupConfig;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.GroupByIdRef;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    public void testCreateGroup() {
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

        groupService.delete(newGroup);
    }

    @Test(groups = {INTEGRATION, LONG_RUNNING})
    public void testUpdateGroup() throws JsonProcessingException {
        Group groupRef = Group.refByName()
            .dataCenter(DataCenter.DE_FRANKFURT)
            .name(Group.DEFAULT_GROUP);
        GroupMetadata groupMetadata = groupService.findByRef(groupRef);
        String parentGroupId = groupMetadata.getParentGroupId();

        String groupName = Group.DEFAULT_GROUP;
        String groupDescription = "test description";
        //TODO identify correct custom fields ids
        List<CustomField> customFields = Arrays.asList(new CustomField().id("123").value("test value"));

        GroupConfig config =
            new GroupConfig()
                .name(groupName)
                .description(groupDescription)
                .parentGroup(Group.refById(parentGroupId));

        groupService.update(groupRef, config).waitUntilComplete();

        GroupMetadata updatedGroupMetadata = groupService.findByRef(groupRef);

        assertEquals(updatedGroupMetadata.getName(), groupName);
        assertEquals(updatedGroupMetadata.getDescription(), groupDescription);
    }

    @Test(groups = {INTEGRATION, LONG_RUNNING})
    public void testUpdateGroups() {
        Group groupRef1 = Group.refByName()
            .dataCenter(DataCenter.DE_FRANKFURT)
            .name(Group.DEFAULT_GROUP);

        Group groupRef2 = Group.refByName()
            .dataCenter(DataCenter.CA_TORONTO_1)
            .name(Group.DEFAULT_GROUP);

        String groupName = Group.DEFAULT_GROUP;
        String groupDescription = "test description";

        GroupConfig config =
            new GroupConfig()
                .name(groupName)
                .description(groupDescription);

        GroupFilter filter = toFilter(groupRef1, groupRef2);
        groupService.update(filter, config).waitUntilComplete();

        groupService.find(filter).stream()
            .forEach(metadata -> assertEquals(metadata.getDescription(), groupDescription));
    }

    @Test(expectedExceptions = ClcException.class)
    public void testUpdateGroupsWithEqualNames() {
        Group groupRef1 = Group.refByName()
            .dataCenter(DataCenter.DE_FRANKFURT)
            .name(Group.DEFAULT_GROUP);

        Group groupRef2 = Group.refByName()
            .dataCenter(DataCenter.CA_TORONTO_1)
            .name(Group.DEFAULT_GROUP);

        String parentGroupId = groupService.findByRef(groupRef1).getParentGroupId();

        groupService.update(
            toFilter(groupRef1, groupRef2),
            new GroupConfig().parentGroup(Group.refById(parentGroupId)));
    }

    @Test(expectedExceptions = ClcException.class)
    public void testUpdateGroupsInSameGroup() {
        Group groupRef1 = Group.refByName()
            .dataCenter(DataCenter.DE_FRANKFURT)
            .name(Group.DEFAULT_GROUP);

        Group groupRef2 = Group.refByName()
            .dataCenter(DataCenter.DE_FRANKFURT)
            .name(Group.ARCHIVE);

        groupService.update(
            toFilter(groupRef1, groupRef2),
            new GroupConfig().name(Group.DEFAULT_GROUP));
    }

    private GroupFilter toFilter(Group... groups) {
        GroupFilter filter = new GroupFilter();
        Arrays.asList(groups).stream()
            .forEach(group ->
                filter.id(groupService.findByRef(group).getId()));

        return filter;
    }

}
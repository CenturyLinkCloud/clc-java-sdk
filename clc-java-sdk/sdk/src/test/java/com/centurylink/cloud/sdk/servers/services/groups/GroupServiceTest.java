package com.centurylink.cloud.sdk.servers.services.groups;

import com.centurylink.cloud.sdk.core.commons.client.domain.CustomField;
import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.DataCenters;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.domain.group.DefaultGroups;
import com.centurylink.cloud.sdk.servers.services.domain.group.Group;
import com.centurylink.cloud.sdk.servers.services.domain.group.GroupConfig;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.GroupRef;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.IdGroupRef;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.NameGroupRef;
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
        List<Group> groups = groupService.findByDataCenter(DataCenters.DE_FRANKFURT);

        assert groups.size() > 0;
    }

    @Test(groups = {INTEGRATION, LONG_RUNNING})
    public void testCreateGroup() {
        String newGroupName = "test group";
        String newGroupDescription = "test group description";
        IdGroupRef newGroup = groupService.create(new GroupConfig()
                .parentGroup(new NameGroupRef(DataCenters.DE_FRANKFURT, DefaultGroups.DEFAULT_GROUP))
                .name(newGroupName)
                .description(newGroupDescription))
            .getResult().as(IdGroupRef.class);

        GroupMetadata createdGroup = groupService.findByRef(newGroup);

        assertEquals(createdGroup.getId(), newGroup.getId());
        assertEquals(createdGroup.getName(), newGroupName);
        assertEquals(createdGroup.getDescription(), newGroupDescription);

        groupService.delete(newGroup);
    }

    @Test(groups = {INTEGRATION, LONG_RUNNING})
    public void testUpdateGroup() throws JsonProcessingException {
        GroupRef groupRef = new NameGroupRef(DataCenters.DE_FRANKFURT, DefaultGroups.DEFAULT_GROUP);
        GroupMetadata groupMetadata = groupService.findByRef(groupRef);
        String parentGroupId = groupMetadata.getParentGroupId();

        String groupName = DefaultGroups.DEFAULT_GROUP;
        String groupDescription = "test description";
        //TODO identify correct custom fields ids
        List<CustomField> customFields = Arrays.asList(new CustomField().id("123").value("test value"));

        GroupConfig config = new GroupConfig()
                .name(groupName)
                .description(groupDescription)
                .parentGroup(new IdGroupRef(DataCenters.DE_FRANKFURT, parentGroupId));

        groupService.update(groupRef, config).waitUntilComplete();

        GroupMetadata updatedGroupMetadata = groupService.findByRef(groupRef);

        assertEquals(updatedGroupMetadata.getName(), groupName);
        assertEquals(updatedGroupMetadata.getDescription(), groupDescription);
    }

}
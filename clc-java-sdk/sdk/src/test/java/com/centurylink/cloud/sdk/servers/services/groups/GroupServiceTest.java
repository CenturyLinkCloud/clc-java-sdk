package com.centurylink.cloud.sdk.servers.services.groups;

import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.DataCenters;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.domain.group.DefaultGroups;
import com.centurylink.cloud.sdk.servers.services.domain.group.Group;
import com.centurylink.cloud.sdk.servers.services.domain.group.GroupConfig;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.NameGroupRef;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;

public class GroupServiceTest extends AbstractServersSdkTest {

    @Inject
    GroupService groupService;

    @Test(groups = INTEGRATION)
    public void testFindGroupsByDataCenter() {
        List<Group> groups = groupService.findByDataCenter(DataCenters.DE_FRANKFURT);

        assert groups.size() > 0;
    }

    @Test(groups = INTEGRATION)
    public void testCreateGroup() {
        GroupMetadata parentGroup = groupService.findByRef(
                new NameGroupRef(DataCenters.DE_FRANKFURT, DefaultGroups.DEFAULT_GROUP)
        );

        String newGroupName = "test group";
        String newGroupDescription = "test group description";
        GroupMetadata newGroup = groupService.create(new GroupConfig()
                .parentGroupId(parentGroup.getId())
                .name(newGroupName)
                .description(newGroupDescription))
            .getResult();

        GroupMetadata createdGroup = groupService.get(newGroup.getId());

        assertEquals(createdGroup.getId(), newGroup.getId());
        assertEquals(createdGroup.getName(), newGroup.getName());
        assertEquals(createdGroup.getDescription(), newGroup.getDescription());

        groupService.delete(createdGroup.getId());
    }

}
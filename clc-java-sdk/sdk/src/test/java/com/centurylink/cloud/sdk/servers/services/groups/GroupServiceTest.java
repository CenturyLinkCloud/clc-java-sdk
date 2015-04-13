package com.centurylink.cloud.sdk.servers.services.groups;

import com.centurylink.cloud.sdk.core.datacenters.services.domain.DataCenters;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.domain.group.Group;
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

}
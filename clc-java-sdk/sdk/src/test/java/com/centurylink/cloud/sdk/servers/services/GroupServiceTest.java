package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.core.datacenters.services.domain.DataCenters;
import com.centurylink.cloud.sdk.servers.services.domain.group.Group;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import java.util.List;

public class GroupServiceTest extends AbstractServersSdkTest {

    @Inject
    private GroupService groupService;

    @Test(groups = "Integration")
    public void testFindGroupsByDataCenter() {
        List<Group> groups = groupService.findByDataCenter(DataCenters.DE_FRANKFURT);

        assert groups.size() > 0;
    }

}
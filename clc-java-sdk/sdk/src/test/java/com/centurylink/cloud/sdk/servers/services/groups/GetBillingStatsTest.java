package com.centurylink.cloud.sdk.servers.services.groups;

import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupBillingStats;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.group.GroupConfig;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.GroupByIdRef;
import com.centurylink.cloud.sdk.servers.services.domain.server.Machine;
import com.centurylink.cloud.sdk.servers.services.servers.TestServerSupport;
import com.google.inject.Inject;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;

@Test(groups = {INTEGRATION, LONG_RUNNING})
public class GetBillingStatsTest extends AbstractServersSdkTest {

    @Inject
    ServerService serverService;

    @Inject
    GroupService groupService;

    ServerMetadata serverMetadata;

    GroupByIdRef group;
    GroupMetadata groupMetadata;

    @BeforeMethod
    public void setUp() {
        initGroup();
        initServer();
    }

    @AfterMethod
    public void tearDown() {
        serverService
            .delete(serverMetadata.asRefById());

        groupService
            .delete(new GroupFilter().nameContains("st-gp"));
    }

    private void initGroup() {
        group = groupService.create(
            new GroupConfig()
                .parentGroup(
                    Group.refByName()
                        .dataCenter(DataCenter.DE_FRANKFURT)
                        .name(Group.DEFAULT_GROUP)
                )
                .name("st-gp")
        )
        .waitUntilComplete()
        .getResult()
        .as(GroupByIdRef.class);

        groupMetadata = groupService.findByRef(group);
    }

    private void initServer() {
        serverMetadata = serverService.create(
            TestServerSupport
                .anyServerConfig()
                    .name("st-srv")
                    .group(group)
                    .machine(
                        new Machine()
                            .cpuCount(1)
                            .ram(2)
                    )
        )
        .waitUntilComplete()
        .getResult();
    }

    @Test
    public void testModifyServer() throws Exception {
        List<GroupBillingStats> stats = groupService.getBillingStats(group);

        /* All changes are not implemented immediately */
        assertNotNull(stats);
        assertEquals(stats.size(), 1);
    }

}

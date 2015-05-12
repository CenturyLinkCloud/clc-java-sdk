package com.centurylink.cloud.sdk.servers.services.groups;

import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.group.BillingStats;
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

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;

@Test(groups = {INTEGRATION, LONG_RUNNING})
public class GetBillingStatsTest extends AbstractServersSdkTest {

    private final static String groupName = "st-gp";

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
            .delete(new GroupFilter().nameContains(groupName));
    }

    private void initGroup() {
        group = groupService.create(
            new GroupConfig()
                .parentGroup(
                    Group.refByName()
                        .dataCenter(DataCenter.DE_FRANKFURT)
                        .name(Group.DEFAULT_GROUP)
                )
                .name(groupName)
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
        BillingStats stats = groupService.getBillingStats(group);

        assertNotNull(stats);
        assertNotNull(stats.getDate());
        assertEquals(stats.getGroups().size(), 1);
        assertEquals(stats.getGroups().get(0).getName(), groupName);

        /* All server changes are not implemented immediately */
//        assertEquals(stats.get(0).getGroups().get(0).getServers().size(), 1);
    }

}

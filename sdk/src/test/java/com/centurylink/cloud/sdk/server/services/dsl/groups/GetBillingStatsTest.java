/*
 * (c) 2015 CenturyLink. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.centurylink.cloud.sdk.server.services.dsl.groups;

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.client.SdkHttpClient;
import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.GroupService;
import com.centurylink.cloud.sdk.server.services.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.BillingStats;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.GroupConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.GroupByIdRef;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.Machine;
import com.centurylink.cloud.sdk.server.services.dsl.servers.TestServerSupport;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ClasspathFileSource;
import com.google.inject.Inject;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@Test(groups = RECORDED)
public class GetBillingStatsTest extends AbstractServersSdkTest {

    private final static String groupName = "st-gp";

    @Inject
    ServerService serverService;

    @Inject
    GroupService groupService;

    ServerMetadata serverMetadata;

    GroupByIdRef group;
    GroupMetadata groupMetadata;

    private WireMockServer wireMockServer;

    @BeforeMethod
    public void setUp() {
        SdkHttpClient.apiUrl("http://localhost:8081/v2");

        wireMockServer = new WireMockServer(wireMockConfig()
            .fileSource(new ClasspathFileSource(
                "com/centurylink/cloud/sdk/servers/services/groups/get_billing_stats"
            ))
            .port(8081)
        );
        wireMockServer.start();

        initGroup();
        initServer();
    }

    @AfterMethod
    public void tearDown() {
        serverService
            .delete(serverMetadata.asRefById());

        groupService
            .delete(group);

        wireMockServer.stop();
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
    }

}

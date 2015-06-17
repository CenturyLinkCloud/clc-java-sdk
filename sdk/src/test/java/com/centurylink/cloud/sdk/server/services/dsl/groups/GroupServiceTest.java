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
import com.centurylink.cloud.sdk.server.services.dsl.GroupService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.GroupConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.GroupByIdRef;
import com.centurylink.cloud.sdk.server.services.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.filters.GroupFilter;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ClasspathFileSource;
import com.google.inject.Inject;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@Test(groups = RECORDED)
public class GroupServiceTest extends AbstractServersSdkTest {

    @Inject
    GroupService groupService;

    private WireMockServer wireMockServer;

    @BeforeMethod
    public void prepareReplay() {
        SdkHttpClient.apiUrl("http://localhost:8081/v2");

        wireMockServer = new WireMockServer(wireMockConfig()
            .fileSource(new ClasspathFileSource(
                "com/centurylink/cloud/sdk/server/services/dsl/groups/operations"
            ))
            .port(8081)
        );
        wireMockServer.start();
    }

    @Test
    public void testFindGroupsByDataCenter() {
        List<GroupMetadata> groups = groupService.find(
            new GroupFilter()
                .dataCenters(DataCenter.DE_FRANKFURT)
        );

        assert groups.size() > 0;
    }

    @Test
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

//        groupService.find(filter).stream()
//            .forEach(metadata -> {
//                assertEquals(metadata.getDescription(), groupDescription);
//                assertEquals(metadata.getName(), groupName);
//            });
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

    @AfterMethod
    public void tearDown() {
        wireMockServer.stop();
        SdkHttpClient.restoreUrl();
    }

}
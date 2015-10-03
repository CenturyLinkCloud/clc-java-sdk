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

import com.centurylink.cloud.sdk.base.services.client.DataCentersClient;
import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.GetDataCenterListResponse;
import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.core.injector.Inject;
import com.centurylink.cloud.sdk.core.services.filter.Filter;
import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.client.ServerClient;
import com.centurylink.cloud.sdk.server.services.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.GroupService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group;
import com.google.common.collect.Sets;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter.DE_FRANKFURT;
import static com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter.GB_PORTSMOUTH;
import static com.centurylink.cloud.sdk.core.function.Streams.map;
import static com.google.common.collect.Sets.newHashSet;

/**
 * @author Ilya Drabenia
 */
public class SearchGroupsTest extends AbstractServersSdkTest {
    final String DE_TEMPLATES_ID = "59a044753cce443e8aae6798dde99682";
    final String GB_TEMPLATES_ID = "02c2895813a0455dafe4b1d0df71a227";
    final String DE_ROOT_ID = "e9cf5a7a9fad43a8a9184d0265ae076c";
    final String GB_ROOT_ID = "295108b591d3487f9f6a3363f769c6e0";
    final String DE_MYSERVER_ID = "06c1d5e1b4a5469a825a91a4bf989b9b";

    @Inject
    GroupService groupService;

    @Inject
    @Mock
    ServerClient serverClient;

    @Inject
    @Mock
    DataCentersClient dataCentersClient;

    @BeforeMethod
    public void setUp() throws Exception {
        Mockito
            .doReturn(fromJson("data_centers_list.json", GetDataCenterListResponse.class))
            .when(dataCentersClient).findAllDataCenters();

        Mockito
            .doReturn(fromJson("de1_root_group.json", GroupMetadata.class))
            .when(serverClient).getGroup(DE_ROOT_ID, true);

        Mockito
            .doReturn(fromJson("gb1_root_group.json", GroupMetadata.class))
            .when(serverClient).getGroup(GB_ROOT_ID, true);
    }

    @Test
    public void testFindByIdRef() {
        GroupMetadata group = groupService.findByRef(Group.refById(DE_ROOT_ID));

        assertEquals(group.getDescription(), "DE1 Hardware");
    }

    @Test
    public void testFindByNameRef() {
        GroupMetadata group = groupService.findByRef(Group.refByName()
            .dataCenter(DE_FRANKFURT)
            .name("Archive")
        );

        assertEquals(group.getName().toLowerCase(), "archive");
    }

    @Test
    public void testFindGroupByName() {
        List<GroupMetadata> groups = groupService.find(new GroupFilter()
            .dataCentersWhere(d -> "de1".equals(d.getId()))
            .nameContains("MyServer")
        );

        assertEquals(groups.get(0).getName(), "MyServer");
    }

    @Test
    public void testFindByNameInAllDataCenters() {
        List<GroupMetadata> groups = groupService.find(new GroupFilter()
            .nameContains("MyServer")
        );

        assertEquals(groups.size(), 1);
        assertEquals(groups.get(0).getName(), "MyServer");
    }

    @Test
    public void testSearchById_allDataCenters() {
        List<GroupMetadata> groups = groupService.find(new GroupFilter()
            .id(GB_ROOT_ID, DE_ROOT_ID)
        );

        assertEquals(groups.get(0).getName(), "GB1 Hardware");
        assertEquals(groups.get(1).getName(), "DE1 Hardware");
    }

    @Test
    public void testSearchById_de1DataCenter() {
        List<GroupMetadata> groups = groupService.find(new GroupFilter()
            .dataCentersWhere(new DataCenterFilter().dataCenters(DE_FRANKFURT))
            .id(GB_TEMPLATES_ID, DE_ROOT_ID)
        );

        assertEquals(groups.size(), 1);
        assertEquals(groups.get(0).getName(), "DE1 Hardware");
    }

    @Test
    public void testAnd_withNames() {
        List<GroupMetadata> groups = groupService.find(Filter.and(
            new GroupFilter()
                .dataCenters(DE_FRANKFURT)
                .names(Group.ARCHIVE, Group.TEMPLATES),

            Filter.or(
                new GroupFilter()
                    .dataCenters(DE_FRANKFURT)
                    .names(Group.TEMPLATES),

                new GroupFilter()
                    .dataCenters(DE_FRANKFURT)
                    .names(Group.DEFAULT_GROUP)
            )
        ));

        assertEquals(groups.size(), 1);
        Assert.assertEquals(groups.get(0).getName(), Group.TEMPLATES);
    }

    @Test
    public void testAnd_withIds() {
        List<GroupMetadata> groups = groupService.find(Filter.and(
            new GroupFilter()
                .dataCenters(DE_FRANKFURT)
                .id(DE_ROOT_ID, DE_MYSERVER_ID),

            new GroupFilter()
                .dataCenters(DE_FRANKFURT)
                .id(DE_ROOT_ID, DE_TEMPLATES_ID)
        ));

        assertEquals(groups.size(), 1);
        assertEquals(groups.get(0).getId(), DE_ROOT_ID);
    }

    @Test
    public void testOr_withNames() {
        List<GroupMetadata> groups = groupService.find(Filter.or(
            new GroupFilter()
                .dataCenters(DE_FRANKFURT)
                .names(Group.ARCHIVE, Group.TEMPLATES),

            new GroupFilter()
                .dataCenters(DE_FRANKFURT)
                .names(Group.TEMPLATES, Group.DEFAULT_GROUP)
        ));

        assertEquals(groups.size(), 3);
        assertEquals(
            newHashSet(map(groups, GroupMetadata::getName)),
            Sets.newHashSet(Group.ARCHIVE, Group.TEMPLATES, Group.DEFAULT_GROUP)
        );
    }

    @Test
    public void testOr_withIds() {
        List<GroupMetadata> groups = groupService.find(Filter.or(
            new GroupFilter()
                .dataCenters(GB_PORTSMOUTH)
                .id(DE_ROOT_ID, GB_ROOT_ID),

            new GroupFilter()
                .dataCenters(DE_FRANKFURT)
                .id(GB_TEMPLATES_ID, DE_ROOT_ID)
        ));

        assertEquals(groups.size(), 2);
        assertEquals(
            newHashSet(map(groups, GroupMetadata::getId)),
            newHashSet(GB_ROOT_ID, DE_ROOT_ID)
        );
    }

}

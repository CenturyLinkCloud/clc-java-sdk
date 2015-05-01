package com.centurylink.cloud.sdk.servers.services.groups;

import com.centurylink.cloud.sdk.core.commons.client.DataCentersClient;
import com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.GetDataCenterListResponse;
import com.centurylink.cloud.sdk.core.services.filter.Filter;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.google.inject.Inject;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenter.DE_FRANKFURT;
import static com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenter.GB_PORTSMOUTH;
import static com.centurylink.cloud.sdk.core.services.function.Streams.map;
import static com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group.ARCHIVE;
import static com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group.DEFAULT_GROUP;
import static com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group.TEMPLATES;
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
    @Spy
    ServerClient serverClient;

    @Inject
    @Spy
    DataCentersClient dataCentersClient;

    @BeforeMethod
    public void setUp() throws Exception {
        Mockito
            .doReturn(fromJson("data_centers_list.json", GetDataCenterListResponse.class))
            .when(dataCentersClient).findAllDataCenters();

        Mockito
            .doReturn(fromJson("de1_root_group.json", GroupMetadata.class))
            .when(serverClient).getGroup(DE_ROOT_ID, false);

        Mockito
            .doReturn(fromJson("gb1_root_group.json", GroupMetadata.class))
            .when(serverClient).getGroup(GB_ROOT_ID, false);
    }

    @Test
    public void testFindByIdRef() {
        GroupMetadata group = groupService.findByRef(Group.refById(DE_ROOT_ID));

        assert group.getDescription().equals("DE1 Hardware");
    }

    @Test
    public void testFindByNameRef() {
        GroupMetadata group = groupService.findByRef(Group.refByName()
            .dataCenter(DE_FRANKFURT)
            .name("Archive")
        );

        assert group.getName().equalsIgnoreCase("archive");
    }

    @Test
    public void testFindGroupByName() {
        List<GroupMetadata> groups = groupService.find(new GroupFilter()
            .dataCenters(DE_FRANKFURT)
            .nameContains("MyServer")
        );

        assert groups.get(0).getName().equals("MyServer");
    }

    @Test
    public void testFindByNameInAllDataCenters() {
        List<GroupMetadata> groups = groupService.find(new GroupFilter()
            .nameContains("MyServer")
        );

        assertEquals(groups.size(), 1);
        assert groups.get(0).getName().equals("MyServer");
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
            .dataCenters(DE_FRANKFURT)
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
                .names(ARCHIVE, TEMPLATES),

            Filter.or(
                new GroupFilter()
                    .dataCenters(DE_FRANKFURT)
                    .names(TEMPLATES),

                new GroupFilter()
                    .dataCenters(DE_FRANKFURT)
                    .names(DEFAULT_GROUP)
            )
        ));

        assertEquals(groups.size(), 1);
        assertEquals(groups.get(0).getName(), TEMPLATES);
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
                .names(ARCHIVE, TEMPLATES),

            new GroupFilter()
                .dataCenters(DE_FRANKFURT)
                .names(TEMPLATES, DEFAULT_GROUP)
        ));

        assertEquals(groups.size(), 3);
        assertEquals(
            newHashSet(map(groups, GroupMetadata::getName)),
            newHashSet(ARCHIVE, TEMPLATES, DEFAULT_GROUP)
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

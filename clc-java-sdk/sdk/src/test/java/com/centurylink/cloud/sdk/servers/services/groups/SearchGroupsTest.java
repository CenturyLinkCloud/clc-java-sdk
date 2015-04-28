package com.centurylink.cloud.sdk.servers.services.groups;

import com.centurylink.cloud.sdk.core.commons.client.DataCentersClient;
import com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.GetDataCenterListResponse;
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

/**
 * @author Ilya Drabenia
 */
public class SearchGroupsTest extends AbstractServersSdkTest {

    @Inject
    GroupService groupService;

    @Inject @Spy
    ServerClient serverClient;

    @Inject @Spy
    DataCentersClient dataCentersClient;

    @BeforeMethod
    public void setUp() throws Exception {
        Mockito
            .doReturn(fromJson("data_centers_list.json", GetDataCenterListResponse.class))
            .when(dataCentersClient).findAllDataCenters();

        Mockito
            .doReturn(fromJson("de1_root_group.json", GroupMetadata.class))
            .when(serverClient).getGroup("e9cf5a7a9fad43a8a9184d0265ae076c", false);

        Mockito
            .doReturn(fromJson("gb1_root_group.json", GroupMetadata.class))
            .when(serverClient).getGroup("295108b591d3487f9f6a3363f769c6e0", false);
    }

    @Test
    public void testFindByIdRef() {
        GroupMetadata group = groupService.findByRef(Group.refById("e9cf5a7a9fad43a8a9184d0265ae076c"));

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
    public void testFindGroupByNameInAllDataCenters() {
        List<GroupMetadata> groups = groupService.find(new GroupFilter()
            .nameContains("MyServer")
        );

        assertEquals(groups.size(), 1);
        assert groups.get(0).getName().equals("MyServer");
    }

}

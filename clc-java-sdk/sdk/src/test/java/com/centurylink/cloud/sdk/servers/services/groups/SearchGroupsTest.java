package com.centurylink.cloud.sdk.servers.services.groups;

import com.centurylink.cloud.sdk.core.client.domain.Link;
import com.centurylink.cloud.sdk.core.datacenters.client.DataCentersClient;
import com.centurylink.cloud.sdk.core.datacenters.client.domain.DataCenterMetadata;
import com.centurylink.cloud.sdk.core.datacenters.services.DataCenterService;
import com.centurylink.cloud.sdk.core.datacenters.services.domain.filters.DataCentersFilter;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.domain.group.Group;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;
import com.google.inject.Inject;
import org.mockito.Mock;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static com.centurylink.cloud.sdk.core.datacenters.services.domain.DataCenters.DE_FRANKFURT;
import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Ilya Drabenia
 */
public class SearchGroupsTest extends AbstractServersSdkTest {

    @Inject
    GroupService groupService;

    @Mock
    ServerClient serverClient;

    @Mock
    DataCenterService dataCenterService;

    @Mock
    DataCentersClient dataCentersClient;

    @Test
    public void testFindByIdRef() {
        when(serverClient.getGroup("1")).thenReturn(new GroupMetadata().id("1"));

        GroupMetadata group = groupService.findByRef(Group.refById()
            .dataCenter(DE_FRANKFURT)
            .id("1")
        );

        assert group.getId().equals("1");
    }

    @Test
    public void testFindByNameRef() {
        when(dataCenterService.findByRef(DE_FRANKFURT)).thenReturn(
            new DataCenterMetadata("DE1", "Frankfurt")
        );

        when(dataCentersClient.getDataCenter("DE1")).thenReturn(
            new DataCenterMetadata() {{
                getLinks().add(new Link() {{
                    setRel("group");
                    setId("rootGroupId");
                }});
            }}
        );

        when(serverClient.getGroup("rootGroupId")).thenReturn(
            new GroupMetadata() {{
                setName("Root Group");
                getGroups().add(new GroupMetadata() {{
                    setName("Archive");
                }});
            }}
        );

        GroupMetadata group = groupService.findByRef(Group.refByName()
            .dataCenter(DE_FRANKFURT)
            .name("Archive")
        );

        assert group.getName().equalsIgnoreCase("archive");
    }

    @Test
    public void testFindGroupByName() {
        when(dataCenterService.find(any(DataCentersFilter.class))).thenReturn(asList(
            new DataCenterMetadata("DE1", "Frankfurt") {{
                getLinks().add(new Link() {{
                    setRel("group");
                    setId("rootGroupId");
                }});
            }}
        ));

        when(serverClient.getGroup("rootGroupId")).thenReturn(
            new GroupMetadata() {{
                setName("Root Group");
                getGroups().add(new GroupMetadata() {{
                    setName("Group1");
                }});
            }}
        );

        List<GroupMetadata> groups = groupService.find(new GroupFilter()
            .dataCenter(DE_FRANKFURT)
            .filter(g -> g.getName().contains("Group1"))
        );

        assert groups.get(0).getName().equals("Group1");
    }

}

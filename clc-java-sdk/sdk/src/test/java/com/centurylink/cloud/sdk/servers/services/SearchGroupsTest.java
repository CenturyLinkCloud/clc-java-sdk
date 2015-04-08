package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.core.client.domain.Link;
import com.centurylink.cloud.sdk.core.datacenters.client.DataCentersClient;
import com.centurylink.cloud.sdk.core.datacenters.client.domain.DataCenterMetadata;
import com.centurylink.cloud.sdk.core.datacenters.services.DataCenterService;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.group.Group;
import com.centurylink.cloud.sdk.tests.mocks.Mocks;
import com.google.inject.Inject;
import org.mockito.Mock;
import org.testng.annotations.Test;

import java.util.Arrays;

import static com.centurylink.cloud.sdk.core.datacenters.services.domain.DataCenters.DE_FRANKFURT;
import static com.centurylink.cloud.sdk.tests.mocks.Mocks.construct;
import static com.centurylink.cloud.sdk.tests.mocks.Mocks.newMock;
import static java.util.Arrays.asList;
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
        when(
            dataCenterService.findByRef(DE_FRANKFURT)
        )
        .thenReturn(
            new DataCenterMetadata("DE1", "Frankfurt")
        );

        when(
            dataCentersClient.getDataCenter("DE1")
        )
        .then(i -> construct(
            new DataCenterMetadata(), dataCenter -> dataCenter.getLinks().add(construct(
                new Link(), link -> {
                    link.setId("1");
                    link.setRel("group");
                }
            )))
        );

        when(serverClient.getGroup("1")).thenReturn(
            construct(new GroupMetadata(), l -> l.setName("Archive"))
        );

        GroupMetadata group = groupService.findByRef(Group.refByName()
            .dataCenter(DE_FRANKFURT)
            .name("Archive")
        );

        assert group.getName().equalsIgnoreCase("archive");
    }

}

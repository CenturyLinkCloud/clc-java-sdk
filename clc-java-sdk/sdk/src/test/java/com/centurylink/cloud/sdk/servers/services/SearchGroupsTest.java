package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.TestModule;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.group.Group;
import com.google.inject.Inject;
import org.mockito.Mock;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.core.datacenters.services.domain.DataCenters.DE_FRANKFURT;
import static org.mockito.Mockito.when;

/**
 * @author Ilya Drabenia
 */
public class SearchGroupsTest extends AbstractServersSdkTest {

    @Inject
    GroupService groupService;

    @Mock
    ServerClient serverClient;

    @Test
    public void testFindByIdRef() {
        when(serverClient.getGroup("1")).thenReturn(new GroupMetadata().id("1"));

        GroupMetadata group = groupService.findByRef(Group.refById()
            .dataCenter(DE_FRANKFURT)
            .id("1")
        );

        assert group.getId().equals("1");
    }

}

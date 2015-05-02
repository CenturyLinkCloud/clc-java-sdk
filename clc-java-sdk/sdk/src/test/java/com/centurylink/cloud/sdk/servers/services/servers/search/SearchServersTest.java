package com.centurylink.cloud.sdk.servers.services.servers.search;

import com.centurylink.cloud.sdk.core.commons.client.DataCentersClient;
import com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.GetDataCenterListResponse;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;
import com.google.inject.Inject;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.eq;

/**
 * @author Ilya Drabenia
 */
public class SearchServersTest extends AbstractServersSdkTest {
    static final String CA2_ROOT_ID = "8c8a2e29f1f24bbaa347c5b2c3d4b791";
    static final String IL1_ROOT_ID = "6949dfcbcb0c459289d91821b04f6eab";

    @Inject
    ServerService serverService;

    @Inject @Spy
    DataCentersClient dataCentersClient;

    @Inject @Spy
    ServerClient serverClient;

    @BeforeMethod
    public void setUpFixtures() {
        Mockito
            .doReturn(fromJson("data_centers_list.json", GetDataCenterListResponse.class))
            .when(dataCentersClient).findAllDataCenters();

        Mockito
            .doReturn(fromJson("ca2_root_group.json", GroupMetadata.class))
            .when(serverClient).getGroup(eq(CA2_ROOT_ID), anyBoolean());

        Mockito
            .doReturn(fromJson("il1_root_group.json", GroupMetadata.class))
            .when(serverClient).getGroup(eq(IL1_ROOT_ID), anyBoolean());
    }

    @Test(enabled = false)
    public void testSearchActiveServers() {
        List<ServerMetadata> results = serverService.find(new ServerFilter()
            .status("archived")
        );

        assertEquals(results.size(), 1);
        assertEquals(results.get(0).getName(), "CA2ALTDARCHIV01");
    }

}

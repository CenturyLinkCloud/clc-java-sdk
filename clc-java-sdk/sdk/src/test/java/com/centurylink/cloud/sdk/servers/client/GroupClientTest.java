package com.centurylink.cloud.sdk.servers.client;

import com.centurylink.cloud.sdk.core.datacenters.client.DataCentersClient;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.group.GetGroupResponse;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.core.datacenters.services.domain.DataCenters.DE_FRANKFURT;

/**
 * @author ilya.drabenia
 */
public class GroupClientTest extends AbstractServersSdkTest {

    @Inject
    private ServerClient client;

    @Inject
    private DataCentersClient dataCentersClient;

    @Test
    public void getGroupsTest() {
        String rootGroupId = dataCentersClient
                .getDataCenter(DE_FRANKFURT.getId())
                .getGroup()
                .getId();

        GetGroupResponse groupResult = client.getGroup(rootGroupId);

        assert groupResult.getId() != null;
        assert groupResult.findGroupByName("Archive") != null;
    }

}
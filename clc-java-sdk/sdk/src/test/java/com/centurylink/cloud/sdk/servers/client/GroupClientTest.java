package com.centurylink.cloud.sdk.servers.client;

import com.centurylink.cloud.sdk.core.commons.client.DataCentersClient;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.DataCenter.DE_FRANKFURT;

/**
 * @author ilya.drabenia
 */
@Test(groups = INTEGRATION)
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

        GroupMetadata groupResult = client.getGroup(rootGroupId, false);

        assert groupResult.getId() != null;
        assert groupResult.findGroupByName("Archive") != null;
    }

}

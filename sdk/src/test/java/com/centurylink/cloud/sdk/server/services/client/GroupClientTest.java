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

package com.centurylink.cloud.sdk.server.services.client;

import com.centurylink.cloud.sdk.base.services.client.DataCentersClient;
import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.client.domain.group.GroupMetadata;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter.DE_FRANKFURT;
import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;

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

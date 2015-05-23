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

package com.centurylink.cloud.sdk.servers.services.servers.remote;

import com.centurylink.cloud.sdk.common.management.services.domain.queue.OperationFuture;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.remote.domain.ShellResponse;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;
import com.google.inject.Inject;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter.US_WEST_SEATTLE;
import static com.centurylink.cloud.sdk.servers.SampleServerConfigs.centOsServer;
import static com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group.DEFAULT_GROUP;
import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;

/**
 * @author Ilya Drabenia
 */
@Test(groups = {INTEGRATION, "LongRunning1"})
public class ExecuteShellCommandOnRemoteServerTest extends AbstractServersSdkTest {

    private Server server;

    @Inject
    ServerService serverService;

    @Inject
    ServerClient serverClient;

    @BeforeClass
    public void createServer() {
        injectDependencies();

        server =
            serverService
                .create(centOsServer("SSHCMD").group(Group.refByName()
                    .dataCenter(US_WEST_SEATTLE)
                    .name(DEFAULT_GROUP)
                ))
                .waitUntilComplete()
                .getResult()
                .asRefById();
    }

    @DataProvider(name = "sshSamples")
    public Object[][] execSshSamples() {
        return new Object[][] {
            {"ping -c google.com", "echo hello"},
            {"mkdir test", "cd ~; pwd"}
        };
    }

    @Test(dataProvider = "sshSamples")
    public void testExecSsh(String shellCommand1, String shellCommand2) throws Exception {
        OperationFuture<ShellResponse> response = serverService.execSsh(server)
            .run(shellCommand1)
            .run(shellCommand2)
            .execute()
            .waitUntilComplete();

        assertNotNull(response);
        assertNotNull(response.getResult().getTrace());
        assertTrue(response.getResult().getErrorStatus() != 1);
    }

    @AfterClass
    public void deleteServer() {
        serverService.delete(server);
    }

}

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

package com.centurylink.cloud.sdk.server.services.dsl.servers.remote;

import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.OperationFuture;
import com.centurylink.cloud.sdk.core.injector.Inject;
import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.client.domain.ip.PublicIpMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group;
import com.centurylink.cloud.sdk.server.services.dsl.domain.ip.CreatePublicIpConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.remote.SshException;
import com.centurylink.cloud.sdk.server.services.dsl.domain.remote.domain.ShellResponse;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.SshConnectionConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.refs.Server;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter.US_WEST_SEATTLE;
import static com.centurylink.cloud.sdk.core.function.Predicates.notNull;
import static com.centurylink.cloud.sdk.server.services.SampleServerConfigs.centOsServer;
import static com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group.DEFAULT_GROUP;
import static com.centurylink.cloud.sdk.server.services.dsl.domain.ip.port.PortConfig.SSH;
import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;
import static java.util.stream.Collectors.toList;

/**
 * @author Ilya Drabenia
 */
@Test(groups = {INTEGRATION, LONG_RUNNING})
public class ExecuteShellCommandOnRemoteServerTest extends AbstractServersSdkTest {

    private Server server;

    @Inject
    ServerService serverService;

    @BeforeClass
    public void createServer() {
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
        return new Object[][]{
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

    @Test
    public void testExecSshWithRunScript() throws Exception {
        String pathToFile = "classpath:ssh-client/classpath-test-ssh.txt";
        testExecSshWithRunScript(pathToFile);
    }

    @Test(expectedExceptions = {SshException.class})
    public void testExecSshWithIncorrectRunScript() throws Exception {
        String pathToFile = "classpath:ssh-client/incorrect-classpath-test-ssh.txt";
        testExecSshWithRunScript(pathToFile);
    }

    private void testExecSshWithRunScript(String pathToFile) {
        OperationFuture<ShellResponse> response = serverService.execSsh(server)
            .runScript(pathToFile)
            .execute()
            .waitUntilComplete();

        assertNotNull(response);
        assertNotNull(response.getResult().getTrace());
        assertTrue(response.getResult().getErrorStatus() != 1);
    }

    @Test(dataProvider = "sshSamples")
    public void testExecSshWithEmptyConfig(String shellCommand1, String shellCommand2) throws Exception {
        SshConnectionConfig sshConnectionConfig = new SshConnectionConfig();
        testExecSshConfig(shellCommand1, shellCommand2, sshConnectionConfig);
    }

    @Test(dataProvider = "sshSamples", expectedExceptions = {SshException.class},
        expectedExceptionsMessageRegExp = "Public IP: 88.48.161.210 is not exist")
    public void testExecSshWithIncorrectPublicIp(String shellCommand1, String shellCommand2) throws Exception {
        testExecSshConfig(shellCommand1, shellCommand2, new SshConnectionConfig()
            .ip("88.48.161.210"));
    }

    @Test(dataProvider = "sshSamples", expectedExceptions = {SshException.class},
        expectedExceptionsMessageRegExp = "Internal IP: 88.48.161.210 is not exist")
    public void testExecSshWithIncorrectInternalIp(String shellCommand1, String shellCommand2) throws Exception {
        testExecSshConfig(shellCommand1, shellCommand2, new SshConnectionConfig()
            .useInternalIp()
            .ip("88.48.161.210"));
    }

    @Test(dataProvider = "sshSamples")
    public void testExecSshWithPublicIp(String shellCommand1, String shellCommand2) throws Exception {
        serverService.addPublicIp(server, new CreatePublicIpConfig().openPorts(SSH)).waitUntilComplete();
        serverService.addPublicIp(server, new CreatePublicIpConfig().openPorts(SSH)).waitUntilComplete();

        findPublicIpWithOpenSshPort().stream().forEach(ip -> {
            testExecSshConfig(shellCommand1, shellCommand2, new SshConnectionConfig().ip(ip));
        });
    }

    private void testExecSshConfig(String shellCommand1, String shellCommand2,
                                   SshConnectionConfig sshConnectionConfig) {
        OperationFuture<ShellResponse> response = serverService.execSsh(server, sshConnectionConfig)
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

    private List<String> findPublicIpWithOpenSshPort() {
        return serverService.findPublicIp(server)
            .stream()
            .filter(ip -> ip
                .getPorts()
                .stream()
                .filter(p -> p.getPort().equals(SSH))
                .count() > 0
            )
            .map(PublicIpMetadata::getPublicIPAddress)
            .filter(notNull())
            .collect(toList());
    }
}

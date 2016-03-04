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

package com.centurylink.cloud.sdk.server.services.dsl.domain.remote;

import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.OperationFuture;
import com.centurylink.cloud.sdk.server.services.client.domain.server.ServerCredentials;
import com.centurylink.cloud.sdk.server.services.dsl.domain.remote.domain.ShellResponse;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Session;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class SshjClientTest {

    @Test
    public void testCreateSshClient() {
        SshjClient client = new SshjClient.Builder()
            .host("10.5.17.29")
            .username("cloud_user")
            .password("cloud_user_password")
            .build();

        assertEquals(client.getHost(), "10.5.17.29");
        assertNotNull(client.getSsh());
    }

    @Test
    public void testExecSsh() {
        SshjClient client = buildSshClient();

        OperationFuture<ShellResponse> response =
            client
                .run("ls -al")
                .run("cd ~")
                .execute();

        assertEquals(response.getResult().getErrorStatus(), 0);
        assertNotNull(response.getResult().getTrace(), "Remote execution command trace");
    }

    private SshjClient buildSshClient() {
        return new SshjClient(
            Mockito.mock(SSHClient.class),
            "10.5.17.29",
            new ServerCredentials().userName("ilya").password("1qa@WS3ed")
        ) {
            @Override
            ShellResponse execCommand(Session session, String command) throws IOException {
                return new ShellResponse(0, "Remote execution command trace");
            }
        };
    }

    @Test
    public void testGetCommandsFromScript() throws Exception {
        assertEquals(new SshjClient(null, null, null)
                .getCommandsFromScript("classpath:ssh-client/classpath-test-ssh.txt"),
            Arrays.asList("ping -c google.com", "echo hello"));
    }

    @Test(expectedExceptions = {NullPointerException.class})
    public void testGetCommandsFromScriptWithIncorrectFilePath() throws Exception {
        new SshjClient(null, null, null).getCommandsFromScript("classpath:ssh-client/incorrect-classpath-test-ssh.txt");
    }
}
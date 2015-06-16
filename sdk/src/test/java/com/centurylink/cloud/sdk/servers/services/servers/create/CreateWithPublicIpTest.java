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

package com.centurylink.cloud.sdk.servers.services.servers.create;

import com.centurylink.cloud.sdk.core.client.SdkHttpClient;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.ip.CreatePublicIpConfig;
import com.centurylink.cloud.sdk.servers.services.domain.server.NetworkConfig;
import com.centurylink.cloud.sdk.servers.services.servers.TestServerSupport;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.ClasspathFileSource;
import com.google.inject.Inject;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;
import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * @author Ilya Drabenia
 */
@Test(groups = RECORDED)
public class CreateWithPublicIpTest extends AbstractServersSdkTest {

    @Inject
    ServerService serverService;

    ServerMetadata server;

    WireMockServer wireMockServer;

    WireMock wireMock;

    @BeforeMethod
    void start() {
        SdkHttpClient.apiUrl("http://localhost:8081/v2");

        wireMockServer = new WireMockServer(wireMockConfig()
            .fileSource(new ClasspathFileSource(
                "com/centurylink/cloud/sdk/servers/services/servers/create/ip"
            ))
            .port(8081)
        );
        wireMockServer.start();

        WireMock.configureFor("localhost", 8081);
        wireMock = new WireMock("localhost", 8081);
    }

    @Test
    public void testCreateServerWithPublicIp() throws Exception {
        server =
            serverService.create(TestServerSupport.anyServerConfig()
                .name("CTTL")
                .network(new NetworkConfig()
                    .publicIpConfig(new CreatePublicIpConfig()
                        .openPorts(8080)
                    )
                )
            )
            .waitUntilComplete()
            .getResult();

        assert !isNullOrEmpty(server.getId());
    }

    @AfterMethod
    public void deleteServer() {
        serverService.delete(server.asRefById().asFilter());
        wireMockServer.stop();

        SdkHttpClient.restoreUrl();
    }

}

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

package sample;

import com.centurylink.cloud.sdk.ClcSdk;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.DefaultCredentialsProvider;
import com.centurylink.cloud.sdk.core.services.refs.ReferenceNotResolvedException;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.ip.CreatePublicIpConfig;
import com.centurylink.cloud.sdk.servers.services.domain.ip.port.PortConfig;
import com.centurylink.cloud.sdk.servers.services.domain.server.CreateServerConfig;
import com.centurylink.cloud.sdk.servers.services.domain.server.DiskConfig;
import com.centurylink.cloud.sdk.servers.services.domain.server.DiskType;
import com.centurylink.cloud.sdk.servers.services.domain.server.Machine;
import com.centurylink.cloud.sdk.servers.services.domain.server.NetworkConfig;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;
import com.centurylink.cloud.sdk.servers.services.domain.template.refs.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.ZonedDateTime;

import static com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter.DE_FRANKFURT;
import static com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter.US_CENTRAL_SALT_LAKE_CITY;
import static com.centurylink.cloud.sdk.servers.services.domain.InfrastructureConfig.dataCenter;
import static com.centurylink.cloud.sdk.servers.services.domain.group.GroupHierarchyConfig.group;
import static com.centurylink.cloud.sdk.servers.services.domain.server.ServerType.STANDARD;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.CpuArchitecture.x86_64;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.OsType.CENTOS;
import static java.util.stream.Collectors.toList;
import static sample.SamplesTestsConstants.SAMPLES;

@Test(groups = {SAMPLES})
public class SuperCommandSampleApp extends Assert {

    private ServerService serverService;
    private GroupService groupService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SuperCommandSampleApp.class);


    public SuperCommandSampleApp() {
        ClcSdk sdk = new ClcSdk(
            new DefaultCredentialsProvider()
        );

        serverService = sdk.serverService();
        groupService = sdk.groupService();
    }

    @BeforeClass
    public void init() {
        clearAll();

        groupService
            .defineInfrastructure(dataCenter(DE_FRANKFURT).subitems(
                group("Sample application").subitems(
                    nginxServer(),

                    group("Business").subitems(
                        apacheHttpServer(),
                        mysqlServer()
                    )
                )
            ))

            .waitUntilComplete();
    }

    @AfterClass
    public void deleteServers() {
        clearAll();
    }

    public static CreateServerConfig centOsServer(String name) {
        return new CreateServerConfig()
            .name(name)
            .description(name)
            .type(STANDARD)
            .machine(new Machine()
                .cpuCount(1)
                .ram(2)
            )
            .template(Template.refByOs()
                .dataCenter(US_CENTRAL_SALT_LAKE_CITY)
                .type(CENTOS)
                .version("6")
                .architecture(x86_64)
            )
            .timeToLive(
                ZonedDateTime.now().plusHours(2)
            );
    }

    public static CreateServerConfig mysqlServer() {
        CreateServerConfig mySqlSrv = centOsServer("MySQL");

        mySqlSrv.getMachine()
            .disk(new DiskConfig()
                .type(DiskType.RAW)
                .size(10)
            );

        return mySqlSrv;
    }

    public static CreateServerConfig nginxServer() {
        return centOsServer("Nginx")
            .network(new NetworkConfig()
                .publicIpConfig(new CreatePublicIpConfig()
                    .openPorts(PortConfig.HTTP)));
    }

    public static CreateServerConfig apacheHttpServer() {
        return centOsServer("Apache");
    }

    private void clearAll() {
        Group ref = Group.refByName(DE_FRANKFURT, "Sample application");

        try {
            groupService.delete(ref);
        } catch (ReferenceNotResolvedException ex) {
            LOGGER.info("nothing to delete", ex);
        }
    }

    private ServerMetadata loadServerMetadata(Server server) {
        ServerMetadata metadata = serverService.findByRef(server);
        assertNotNull(metadata);

        return metadata;
    }

    private void checkServerIsStarted(String name) {
        assert
            serverService
                .findLazy(new ServerFilter()
                    .onlyActive()
                    .descriptionContains(name)
                )
                .findFirst().get()
                .getDetails()
                .getPowerState()
                .equals("started");
    }

    @Test
    public void checkServersIsActiveTest() {
        checkServerIsStarted("nginx");
        checkServerIsStarted("mysql");
        checkServerIsStarted("apache");
    }

    @Test
    public void nginxTest() {
        ServerMetadata nginxMetadata = loadServerMetadata(
            Server.refByDescription(DE_FRANKFURT, "nginx")
        );

        assert
            nginxMetadata.getDetails().getIpAddresses().stream()
            .filter(address -> address.getPublicIp() != null)
            .collect(toList())
            .size() == 1;
    }
}

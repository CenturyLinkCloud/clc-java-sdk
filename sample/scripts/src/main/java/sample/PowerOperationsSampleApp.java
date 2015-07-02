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
import com.centurylink.cloud.sdk.server.services.dsl.GroupService;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.CreateServerConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.Machine;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.filters.ServerFilter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.refs.Server;
import com.centurylink.cloud.sdk.server.services.dsl.domain.template.refs.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.ZonedDateTime;

import static com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter.US_CENTRAL_SALT_LAKE_CITY;
import static com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter.US_EAST_STERLING;
import static com.centurylink.cloud.sdk.server.services.dsl.domain.InfrastructureConfig.dataCenter;
import static com.centurylink.cloud.sdk.server.services.dsl.domain.group.GroupHierarchyConfig.group;
import static com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group.DEFAULT_GROUP;
import static com.centurylink.cloud.sdk.server.services.dsl.domain.server.ServerType.STANDARD;
import static com.centurylink.cloud.sdk.server.services.dsl.domain.template.filters.os.CpuArchitecture.x86_64;
import static com.centurylink.cloud.sdk.server.services.dsl.domain.template.filters.os.OsType.CENTOS;
import static java.lang.Boolean.TRUE;
import static sample.SamplesTestsConstants.SAMPLES;

@Test(groups = {SAMPLES})
public class PowerOperationsSampleApp extends Assert {

    private final ServerService serverService;
    private final GroupService groupService;

    private static final Logger LOGGER = LoggerFactory.getLogger(PowerOperationsSampleApp.class);

    private static final String GROUP_NAME = "MyServers";

    public PowerOperationsSampleApp() {
        ClcSdk sdk = new ClcSdk(
            new DefaultCredentialsProvider()
        );

        serverService = sdk.serverService();
        groupService = sdk.groupService();
    }

    public static CreateServerConfig centOsServer(String name) {
        return new CreateServerConfig()
            .name("PWROPS")
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
            .timeToLive(ZonedDateTime.now().plusHours(2));
    }

    @BeforeClass
    public void init() {
        try {
            deleteServers();
        } catch (Exception ex) {
            LOGGER.info("nothing to delete", ex);
        }

        groupService
            .defineInfrastructure(dataCenter(US_EAST_STERLING).subitems(
                group(DEFAULT_GROUP).subitems(
                    group(GROUP_NAME,
                        "MyServers Group Description").subitems(
                        centOsServer("a_nginx"),
                        centOsServer("a_apache"),
                        centOsServer("b_mysql")
                    )
                )
            ))

            .waitUntilComplete();
    }

    @AfterClass
    public void deleteServers() {
        serverService
            .delete(new ServerFilter()
                .dataCenters(US_EAST_STERLING)
                .groupNames(GROUP_NAME)
                .onlyActive()
            )
            .waitUntilComplete();

        groupService
            .delete(new GroupFilter()
                .dataCenters(US_EAST_STERLING)
                .names(GROUP_NAME)
            )
            .waitUntilComplete();
    }

    private Group myServersGroup() {
        return Group.refByName()
            .dataCenter(US_EAST_STERLING)
            .name(GROUP_NAME);
    }

    private Server getMysqlServer() {
        return
            Server.refByDescription()
                .dataCenter(US_EAST_STERLING)
                .description("b_mysql");
    }

    @Test
    public void testWholeGroupReboot() {
        groupService
            .reboot(myServersGroup())
            .waitUntilComplete();

        assert
            serverService
                .findLazy(new ServerFilter().groups(myServersGroup()))
                .filter(s -> "started".equals(s.getDetails().getPowerState()))
                .count() == 3;
    }

    @Test
    public void testCreateSnapshots() {
        serverService
            .createSnapshot(new ServerFilter()
                .dataCenters(US_EAST_STERLING)
                .groupNames(GROUP_NAME)
                .descriptionContains("a_")
            )
            .waitUntilComplete();

        assert
            serverService
                .findByRef(Server.refByDescription()
                    .dataCenter(US_EAST_STERLING)
                    .description("a_apache")
                )
                .getDetails().getSnapshots().size() == 1;
    }

    @Test(enabled = false)
    public void testStartMaintenanceMode() {
        groupService
            .startMaintenance(myServersGroup())
            .waitUntilComplete();

        assert
            serverService
                .findLazy(new ServerFilter().groups(myServersGroup()))
                .filter(s -> TRUE.equals(s.getDetails().getInMaintenanceMode()))
                .count() == 3;
    }

    @Test
    public void testStopMaintenanceMode() {
        testStartMaintenanceMode();

        groupService
            .stopMaintenance(myServersGroup())
            .waitUntilComplete();

        assert
            serverService
                .findLazy(new ServerFilter().groups(myServersGroup()))
                .filter(s -> !TRUE.equals(s.getDetails().getInMaintenanceMode()))
                .count() == 3L;
    }


    @Test(enabled = false)
    public void testStopMySql() {
        serverService
            .powerOff(new ServerFilter()
                .dataCenters(US_EAST_STERLING)
                .groupNames(GROUP_NAME)
                .descriptionContains("b_")
            )
            .waitUntilComplete();

        assertEquals(
            "stopped",
            serverService
                .findByRef(getMysqlServer())
                .getDetails()
                .getPowerState()
        );
    }

    @Test
    public void testStartMySql() {
        testStopMySql();

        serverService
            .powerOn(getMysqlServer())
            .waitUntilComplete();

        assertEquals(
            "started",
            serverService
                .findByRef(getMysqlServer())
                .getDetails()
                .getPowerState()
        );
    }

}

package sample;

import com.centurylink.cloud.sdk.ClcSdk;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.PropertiesFileCredentialsProvider;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.InfrastructureConfig;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.ip.CreatePublicIpConfig;
import com.centurylink.cloud.sdk.servers.services.domain.ip.port.PortConfig;
import com.centurylink.cloud.sdk.servers.services.domain.server.*;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;
import com.centurylink.cloud.sdk.servers.services.domain.template.refs.Template;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.ZonedDateTime;

import static com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter.US_CENTRAL_SALT_LAKE_CITY;
import static com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter.US_EAST_STERLING;
import static com.centurylink.cloud.sdk.servers.services.domain.InfrastructureConfig.dataCenter;
import static com.centurylink.cloud.sdk.servers.services.domain.group.GroupHierarchyConfig.group;
import static com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group.DEFAULT_GROUP;
import static com.centurylink.cloud.sdk.servers.services.domain.server.CreateServerConfig.*;
import static com.centurylink.cloud.sdk.servers.services.domain.server.ServerType.STANDARD;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.CpuArchitecture.x86_64;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.OsType.CENTOS;
import static com.centurylink.cloud.sdk.tests.TestGroups.SAMPLES;
import static java.lang.Boolean.TRUE;

@Test(groups = SAMPLES)
public class PowerOperationsSampleApp extends Assert {
    private final ServerService serverService;
    private final GroupService groupService;

    public PowerOperationsSampleApp() {
        ClcSdk sdk = new ClcSdk(
            new PropertiesFileCredentialsProvider("centurylink-clc-sdk-uat.properties")
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
        deleteServers();

        groupService
            .defineInfrastructure(dataCenter(US_EAST_STERLING).subitems(
                group(DEFAULT_GROUP).subitems(
                    group("MyServers",
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
                .groupNames("MyServers")
            )
            .waitUntilComplete();

        groupService
            .delete(new GroupFilter()
                .dataCenters(US_EAST_STERLING)
                .names("MyServers")
            )
            .waitUntilComplete();
    }

    private Group myServersGroup() {
        return Group.refByName()
            .dataCenter(US_EAST_STERLING)
            .name("MyServers");
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
                .filter(s -> s.getDetails().getPowerState().equals("started"))
                .count() == 3;
    }

    @Test
    public void testCreateSnapshots() {
        serverService
            .createSnapshot(new ServerFilter()
                .dataCenters(US_EAST_STERLING)
                .groupNames("MyServers")
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
                .groupNames("MyServers")
                .descriptionContains("b_")
            )
            .waitUntilComplete();

        assert
            serverService
                .findByRef(getMysqlServer())
                .getDetails()
                .getPowerState()
                .equals("stopped");
    }

    @Test
    public void testStartMySql() {
        testStopMySql();

        serverService
            .powerOn(getMysqlServer())
            .waitUntilComplete();

        assert
            serverService
                .findByRef(getMysqlServer())
                .getDetails()
                .getPowerState()
                .equals("started");
    }

}

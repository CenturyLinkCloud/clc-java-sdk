package sample;

import com.centurylink.cloud.sdk.ClcSdk;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenterByIdRef;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.future.OperationFuture;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.PropertiesFileCredentialsProvider;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.TemplateService;
import com.centurylink.cloud.sdk.servers.services.domain.group.GroupConfig;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.GroupByIdRef;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.GroupNameRef;
import com.centurylink.cloud.sdk.servers.services.domain.server.CreateServerConfig;
import com.centurylink.cloud.sdk.servers.services.domain.server.Machine;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;
import com.centurylink.cloud.sdk.servers.services.domain.template.refs.Template;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.ZonedDateTime;

import static com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter.US_EAST_STERLING;
import static com.centurylink.cloud.sdk.servers.services.domain.server.ServerType.STANDARD;
import static com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server.refByDescription;
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

    @BeforeClass
    public void init() {
        deleteServers();

        createGroup(US_EAST_STERLING, "MyServers", "MyServers Group Description");

        OperationFuture.waitUntilComplete(
            createServer("a_nginx"),
            createServer("a_apache"),
            createServer("b_mysql")
        );
    }

    @AfterClass
    public void deleteServers() {
        groupService
            .delete(new GroupFilter()
                .dataCenters(US_EAST_STERLING)
                .names("MyServers")
            )
            .waitUntilComplete();
    }

    private OperationFuture<ServerMetadata> createServer(String name) {
        return
            serverService
                .create(new CreateServerConfig()
                    .name("PWROPS")
                    .description(name)
                    .type(STANDARD)
                    .group(Group.refByName(US_EAST_STERLING, "MyServers"))
                    .timeToLive(ZonedDateTime.now().plusDays(1))
                    .machine(new Machine()
                        .cpuCount(1)
                        .ram(3)
                    )
                    .template(Template.refByOs()
                        .dataCenter(US_EAST_STERLING)
                        .type(CENTOS)
                        .version("6")
                        .architecture(x86_64)
                    )
                );
    }

    private GroupByIdRef createGroup(DataCenterByIdRef dataCenter, String name, String description) {
        return
            groupService.create(
                new GroupConfig()
                    .parentGroup(Group.refByName()
                        .dataCenter(dataCenter)
                        .name(Group.DEFAULT_GROUP)
                    )
                    .name(name)
                    .description(description)
            )
            .waitUntilComplete()
            .getResult()
            .as(GroupByIdRef.class);
    }

    private Group myServersGroup() {
        return Group.refByName()
            .dataCenter(US_EAST_STERLING)
            .name("MyServers");
    }

    private Server mysqlServer() {
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
                .findByRef(mysqlServer())
                .getDetails()
                .getPowerState()
                .equals("stopped");
    }

    @Test
    public void testStartMySql() {
        testStopMySql();

        serverService
            .powerOn(mysqlServer())
            .waitUntilComplete();

        assert
            serverService
                .findByRef(mysqlServer())
                .getDetails()
                .getPowerState()
                .equals("started");
    }

}

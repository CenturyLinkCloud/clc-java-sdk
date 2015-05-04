package sample;

import com.centurylink.cloud.sdk.ClcSdk;
import com.centurylink.cloud.sdk.common.management.client.domain.datacenters.deployment.capabilities.TemplateMetadata;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenterByIdRef;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.future.OperationFuture;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.PropertiesFileCredentialsProvider;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.TemplateService;
import com.centurylink.cloud.sdk.servers.services.domain.group.GroupConfig;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.GroupByIdRef;
import com.centurylink.cloud.sdk.servers.services.domain.server.CreateServerConfig;
import com.centurylink.cloud.sdk.servers.services.domain.server.DiskConfig;
import com.centurylink.cloud.sdk.servers.services.domain.server.DiskType;
import com.centurylink.cloud.sdk.servers.services.domain.server.Machine;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.ServerByIdRef;
import com.centurylink.cloud.sdk.servers.services.domain.template.filters.TemplateFilter;
import com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.OsFilter;
import com.centurylink.cloud.sdk.servers.services.domain.template.refs.Template;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter.US_EAST_STERLING;
import static com.centurylink.cloud.sdk.servers.services.domain.server.ServerType.STANDARD;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.CpuArchitecture.x86_64;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.OsType.CENTOS;
import static com.centurylink.cloud.sdk.tests.TestGroups.SAMPLES;
import static java.util.stream.Collectors.toList;

@Test(groups = SAMPLES)
public class PowerOperationsSampleApp extends Assert {
    private final static String MY_SERVERS_GROUP = "MyServer";

    private final ServerService serverService;
    private final GroupService groupService;
    private final TemplateService templateService;

    public PowerOperationsSampleApp() {
        ClcSdk sdk = new ClcSdk(
            new PropertiesFileCredentialsProvider("centurylink-clc-sdk-uat.properties")
        );

        serverService = sdk.serverService();
        groupService = sdk.groupService();
        templateService = sdk.templateService();
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

    private ServerMetadata loadServerMetadata(Server server) {
        ServerMetadata metadata = serverService.findByRef(server);
        assertNotNull(metadata);

        return metadata;
    }

    @Test
    public void testGroupReboot() {
        groupService
            .reboot(Group.refByName()
                .dataCenter(US_EAST_STERLING)
                .name("MyServers")
            )
            .waitUntilComplete();

        assertEquals(
            loadServerMetadata(Server.refByDescription(US_EAST_STERLING, "a_nginx"))
                .getDetails()
                .getPowerState(),
            "started"
        );
    }

}

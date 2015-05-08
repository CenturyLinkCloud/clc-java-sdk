package sample;

import com.centurylink.cloud.sdk.ClcSdk;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.PropertiesFileCredentialsProvider;
import com.centurylink.cloud.sdk.core.services.refs.ReferenceNotResolvedException;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.InfrastructureConfig;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.GroupByIdRef;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.ServerByIdRef;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.servers.services.domain.group.GroupHierarchyConfig.group;
import static com.centurylink.cloud.sdk.servers.services.domain.server.CreateServerConfig.*;
import static com.centurylink.cloud.sdk.tests.TestGroups.SAMPLES;
import static java.util.stream.Collectors.toList;

@Test(groups = SAMPLES)
public class SuperCommandSampleApp extends Assert {

    private final static String rootGroupName = "Sample application";
    private final static String businessGroupName = "Business";

    private ServerService serverService;
    private GroupService groupService;

    private GroupByIdRef rootGroup;
    private GroupByIdRef businessGroup;

    ServerByIdRef nginx;
    ServerByIdRef apache;
    ServerByIdRef mysql;

    @BeforeClass
    public void init() {
        ClcSdk sdk = new ClcSdk(
            new PropertiesFileCredentialsProvider("centurylink-clc-sdk-uat.properties")
        );

        serverService = sdk.serverService();
        groupService = sdk.groupService();

        clearAll();

        rootGroup = groupService.defineInfrastructure(new InfrastructureConfig()
            .datacenter(DataCenter.DE_FRANKFURT)
            .subitems(group(rootGroupName)
                    .subitems(group(businessGroupName)
                            .subitems(
                                apacheHttpServer(),
                                mysqlServer()
                            ),
                        nginxServer()
                    )
            )
        ).waitUntilComplete().getResult().get(0).as(GroupByIdRef.class);

        businessGroup = Group.refById(groupService.findByRef(rootGroup).getGroups().get(0).getId());

        List<ServerMetadata> rootServers = groupService.findByRef(rootGroup).getServers();
        List<ServerMetadata> businessServers = groupService.findByRef(businessGroup).getServers();

        nginx = rootServers.stream()
            .filter(server -> server.getName().contains("NGINX"))
            .findFirst()
            .get().asRefById();
        mysql = businessServers.stream()
            .filter(server -> server.getName().contains("MYSQL"))
            .findFirst()
            .get().asRefById();
        apache = businessServers.stream()
            .filter(server -> server.getName().contains("APACHE"))
            .findFirst()
            .get().asRefById();
    }

    @AfterClass
    public void deleteServers() {
        clearAll();
    }

    private void clearAll() {
        Group ref = Group.refByName(DataCenter.DE_FRANKFURT, rootGroupName);
        try {
            groupService.delete(ref);
        } catch (ReferenceNotResolvedException ex) {}
    }

    private ServerMetadata loadServerMetadata(Server server) {
        ServerMetadata metadata = serverService.findByRef(server);
        assertNotNull(metadata);

        return metadata;
    }

    private void checkServerIsStarted(Server server) {
        assert
            serverService
                .findByRef(server)
                .getDetails()
                .getPowerState()
                .equals("started");
    }

    @Test
    public void checkServersIsActiveTest() {
        checkServerIsStarted(nginx);
        checkServerIsStarted(mysql);
        checkServerIsStarted(apache);
    }

    @Test
    public void nginxTest() {
        ServerMetadata nginxMetadata = loadServerMetadata(nginx);

        assert
            nginxMetadata.getDetails().getIpAddresses().stream()
            .filter(address -> address.getPublicIp() != null)
            .collect(toList())
            .size() == 1;
    }
}

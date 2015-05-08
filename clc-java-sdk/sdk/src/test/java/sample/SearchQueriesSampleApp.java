package sample;

import com.centurylink.cloud.sdk.ClcSdk;
import com.centurylink.cloud.sdk.common.management.client.domain.datacenters.deployment.capabilities.TemplateMetadata;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenterByIdRef;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.future.OperationFuture;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.PropertiesFileCredentialsProvider;
import com.centurylink.cloud.sdk.core.function.Streams;
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

import static com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter.CA_VANCOUVER;
import static com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter.DE_FRANKFURT;
import static com.centurylink.cloud.sdk.servers.services.domain.server.ServerType.STANDARD;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.CpuArchitecture.x86_64;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.OsType.CENTOS;
import static com.centurylink.cloud.sdk.tests.TestGroups.SAMPLES;
import static java.util.stream.Collectors.toList;

public class SearchQueriesSampleApp extends Assert {

    private final static String group1Name = "uat1";
    private final static String group2Name = "uat2";

    private ServerService serverService;
    private GroupService groupService;
    private TemplateService templateService;

    /* group 1 */
    private GroupByIdRef group1De;
    private GroupByIdRef group1Va;

    /* group 2 */
    private GroupByIdRef group2De;

    /* DE1 datacenter */
    ServerByIdRef server1De;
    ServerByIdRef server2De;

    /* VA1 datacenter */
    ServerByIdRef server1Va;
    ServerByIdRef server2Va;

    @BeforeClass(groups = SAMPLES)
    public void init() {
        ClcSdk sdk = new ClcSdk(
            new PropertiesFileCredentialsProvider("centurylink-clc-sdk-uat.properties")
        );

        serverService = sdk.serverService();
        groupService = sdk.groupService();
        templateService = sdk.templateService();

        clearAll();

        group1De = createGroup(DE_FRANKFURT, group1Name, "uat1 group description");
        group1Va = createGroup(DataCenter.US_EAST_STERLING, group1Name, "uat1 group description");
        group2De = createGroup(DE_FRANKFURT, group2Name, "uat2 group description");

        List<ServerByIdRef> results =
            OperationFuture.waitUntilComplete(
                createServer(DE_FRANKFURT, group1De, "sr-de1"),
                createServer(DE_FRANKFURT, group2De, "sr-de2"),
                createServer(CA_VANCOUVER, group1Va, "sr-va1"),
                createServer(CA_VANCOUVER, group1Va, "sr-va2")
            )
            .getResult().stream()
            .map(ServerMetadata::asRefById)
            .collect(toList());

        server1De = results.get(0);
        server2De = results.get(1);
        server1Va = results.get(2);
        server2Va = results.get(3);

        serverService
            .archive(server2Va)
            .waitUntilComplete();

        assertThatServerHasStatus(server2Va, "archived");
    }

    @AfterClass(groups = SAMPLES)
    public void deleteServers() {
        clearAll();
    }

    private OperationFuture<ServerMetadata> createServer(DataCenterByIdRef dataCenter, Group group, String name) {
        return
            serverService
                .create(new CreateServerConfig()
                    .name(name)
                    .type(STANDARD)
                    .group(group)
                    .timeToLive(ZonedDateTime.now().plusDays(1))
                    .machine(new Machine()
                        .cpuCount(1)
                        .ram(3)
                        .disk(new DiskConfig()
                            .type(DiskType.RAW)
                            .size(14)
                        )
                    )
                    .template(Template.refByOs()
                        .dataCenter(dataCenter)
                        .type(CENTOS)
                        .version("6")
                        .architecture(x86_64)
                    )
                );
    }

    private void clearAll() {
        serverService
            .delete(new ServerFilter()
                .dataCenters(DE_FRANKFURT, CA_VANCOUVER)
            )
            .waitUntilComplete();

        groupService
            .delete(new GroupFilter().nameContains("uat"))
            .waitUntilComplete();
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

    private void assertThatServerHasStatus(Server server, String status) {
        assertEquals(loadServerMetadata(server).getStatus(), status);
    }

    private void checkServerMetadataList(List<ServerMetadata> serverMetadataList, List<String> expectedServerIdList) {
        assertNotNull(serverMetadataList);
        assertEquals(serverMetadataList.size(), expectedServerIdList.size());

        serverMetadataList.forEach(serverMetadata ->
            assertTrue(
                expectedServerIdList.contains(serverMetadata.getId())
            )
        );
    }

    private void checkGroupMetadataList(List<GroupMetadata> groupMetadataList, List<String> expectedGroupIdList) {
        assertNotNull(groupMetadataList);
        assertEquals(groupMetadataList.size(), expectedGroupIdList.size());

        groupMetadataList.forEach(serverMetadata ->
            assertTrue(
                expectedGroupIdList.contains(serverMetadata.getId())
            )
        );
    }

    /**
     * Step 1. List all servers available for current user
     */
    @Test(groups = SAMPLES)
    public void findAllServersTest() {
        List<ServerMetadata> serverMetadataList = serverService.find(
            new ServerFilter()
        );

        List<String> serverIdList = new ArrayList<>();
        serverIdList.add(server1De.getId());
        serverIdList.add(server2De.getId());
        serverIdList.add(server1Va.getId());
        serverIdList.add(server2Va.getId());

        checkServerMetadataList(serverMetadataList, serverIdList);
    }

    /**
     * Step 2. Find all active servers in all datacenters
     */
    @Test(groups = SAMPLES)
    public void findAllActiveServersTest() {
        List<ServerMetadata> serverMetadataList = serverService.find(
            new ServerFilter().onlyActive()
        );

        List<String> serverIdList = new ArrayList<>();
        serverIdList.add(server1De.getId());
        serverIdList.add(server2De.getId());
        serverIdList.add(server1Va.getId());

        checkServerMetadataList(serverMetadataList, serverIdList);
    }

    /**
     * Step 3. Find server within "uta1" group in all datacenters
     */
    @Test(groups = SAMPLES)
    public void findGroupServersTest() {
        List<ServerMetadata> serverMetadataList = serverService.find(
            new ServerFilter().groupNameContains(group1Name)
        );

        List<String> serverIdList = new ArrayList<>();
        serverIdList.add(server1De.getId());
        serverIdList.add(server1Va.getId());

        checkServerMetadataList(serverMetadataList, serverIdList);
    }

    /**
     * Step 4. Find server that contains some value in it's metadata
     */
    @Test(groups = SAMPLES)
    public void findServersByMetadataValueTest() {
        String keyword = "sr-de";

        List<ServerMetadata> serverMetadataList = serverService.find(
            new ServerFilter().where(
                serverMetadata -> serverMetadata.getName().toLowerCase().contains(keyword)
            )
        );

        List<String> serverIdList = new ArrayList<>();
        serverIdList.add(server1De.getId());
        serverIdList.add(server2De.getId());

        checkServerMetadataList(serverMetadataList, serverIdList);
    }

    /**
     * Step 5. Find templates of specified operating system
     */
    @Test(groups = SAMPLES)
    public void findOsTemplatesTest() {
        List<TemplateMetadata> templateMetadataList = templateService.find(
            new TemplateFilter()
                .dataCenters(DE_FRANKFURT)
                .osTypes(new OsFilter().type(CENTOS))
        );

        assertNotNull(templateMetadataList);
        assertEquals(templateMetadataList.size(), 2);
    }

    /**
     * Step 6. Find groups that contains keyword in description
     */
    @Test(groups = SAMPLES)
    public void testFindGroupsByDescriptionKeyword() {
        String keyword = group1Name;

        List<GroupMetadata> groupMetadataList = groupService.find(
            new GroupFilter()
                .dataCenters(DE_FRANKFURT)
                .where(
                    groupMetadata -> groupMetadata.getDescription().contains(keyword)
                )
        );

        List<String> groupIdList = new ArrayList<>();
        groupIdList.add(group1De.getId());

        checkGroupMetadataList(groupMetadataList, groupIdList);
    }
}

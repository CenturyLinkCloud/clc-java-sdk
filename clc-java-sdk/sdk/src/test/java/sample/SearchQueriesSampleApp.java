package sample;

import com.centurylink.cloud.sdk.ClcSdk;
import com.centurylink.cloud.sdk.common.management.client.domain.datacenters.deployment.capabilities.TemplateMetadata;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.PropertiesFileCredentialsProvider;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.TemplateService;
import com.centurylink.cloud.sdk.servers.services.domain.InfrastructureConfig;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.GroupByIdRef;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.ServerByIdRef;
import com.centurylink.cloud.sdk.servers.services.domain.template.filters.TemplateFilter;
import com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.OsFilter;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.centurylink.cloud.sdk.servers.services.domain.group.GroupHierarchyConfig.group;
import static com.centurylink.cloud.sdk.servers.services.domain.server.CreateServerConfig.baseServerConfig;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.OsType.CENTOS;
import static com.centurylink.cloud.sdk.tests.TestGroups.SAMPLES;

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

        List<Group> groups = groupService.defineInfrastructure(new InfrastructureConfig()
                .datacenter(DataCenter.DE_FRANKFURT)
                .subitems(group(group1Name)
                        .description("uat1 group description")
                        .subitems(baseServerConfig()
                            .name("sr-de1")),
                    group(group2Name)
                        .description("uat2 group description")
                        .subitems(baseServerConfig()
                                .name("sr-de2")
                        )
                ),
            new InfrastructureConfig()
                .datacenter(DataCenter.US_EAST_STERLING)
                .subitems(group(group1Name)
                        .description("uat1 group description")
                        .subitems(
                            baseServerConfig().name("sr-va1"),
                            baseServerConfig().name("sr-va2")
                        )
                )
        ).waitUntilComplete().getResult();


        groups.forEach(group -> {
            GroupMetadata metadata = groupService.findByRef(group);

            switch (metadata.getName()) {
                case group1Name: {
                    if (metadata.getLocationId().equals("DE1")) {
                        group1De = Group.refById(metadata.getId());
                    } else {
                        group1Va = Group.refById(metadata.getId());
                    }
                    break;
                }
                case group2Name: group2De = Group.refById(metadata.getId());break;
            }
        });

        List<ServerMetadata> serversDe1 = groupService.findByRef(group1De).getServers();
        List<ServerMetadata> serversDe2 = groupService.findByRef(group2De).getServers();
        List<ServerMetadata> serversVa1 = groupService.findByRef(group1Va).getServers();

        server1De = serversDe1.stream()
            .filter(server -> server.getName().contains("sr-de1".toUpperCase()))
            .findFirst()
            .get().asRefById();
        server2De = serversDe2.stream()
            .filter(server -> server.getName().contains("sr-de2".toUpperCase()))
            .findFirst()
            .get().asRefById();
        server1Va = serversVa1.stream()
            .filter(server -> server.getName().contains("sr-va1".toUpperCase()))
            .findFirst()
            .get().asRefById();
        server2Va = serversVa1.stream()
            .filter(server -> server.getName().contains("sr-va2".toUpperCase()))
            .findFirst()
            .get().asRefById();

        serverService
            .archive(server2Va)
            .waitUntilComplete();

        assertThatServerHasStatus(server2Va, "archived");
    }

    @AfterClass(groups = SAMPLES)
    public void deleteServers() {
        clearAll();
    }

    private void clearAll() {
        serverService.delete(new ServerFilter()).waitUntilComplete();
        List<GroupMetadata> groupMetadataList = groupService.find(
            new GroupFilter().nameContains("uat")
        );

        groupMetadataList.forEach(groupMetadata ->
            groupService.delete(
                Group.refById(groupMetadata.getId())
            )
        );
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
                .dataCenters(DataCenter.DE_FRANKFURT)
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
                .dataCenters(DataCenter.DE_FRANKFURT)
                .where(
                    groupMetadata -> groupMetadata.getDescription().contains(keyword)
                )
        );

        List<String> groupIdList = new ArrayList<>();
        groupIdList.add(group1De.getId());

        checkGroupMetadataList(groupMetadataList, groupIdList);
    }
}

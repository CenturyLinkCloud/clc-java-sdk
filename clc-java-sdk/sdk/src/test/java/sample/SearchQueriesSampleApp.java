package sample;

import com.centurylink.cloud.sdk.ClcSdk;
import com.centurylink.cloud.sdk.common.management.client.domain.datacenters.deployment.capabilities.TemplateMetadata;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.PropertiesFileCredentialsProvider;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.TemplateService;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.servers.services.domain.server.CreateServerConfig;
import com.centurylink.cloud.sdk.servers.services.domain.server.Machine;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;
import com.centurylink.cloud.sdk.servers.services.domain.template.filters.TemplateFilter;
import com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.OsFilter;
import com.centurylink.cloud.sdk.servers.services.domain.template.refs.Template;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.ZonedDateTime;
import java.util.List;

import static com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter.*;
import static com.centurylink.cloud.sdk.core.function.Streams.map;
import static com.centurylink.cloud.sdk.servers.services.domain.InfrastructureConfig.dataCenter;
import static com.centurylink.cloud.sdk.servers.services.domain.group.GroupHierarchyConfig.group;
import static com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group.DEFAULT_GROUP;
import static com.centurylink.cloud.sdk.servers.services.domain.server.ServerStatus.ACTIVE;
import static com.centurylink.cloud.sdk.servers.services.domain.server.ServerStatus.ARCHIVED;
import static com.centurylink.cloud.sdk.servers.services.domain.server.ServerType.STANDARD;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.CpuArchitecture.x86_64;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.OsType.CENTOS;
import static com.centurylink.cloud.sdk.tests.TestGroups.SAMPLES;
import static com.google.common.collect.Sets.newHashSet;

public class SearchQueriesSampleApp extends Assert {

    private ServerService serverService;
    private GroupService groupService;
    private TemplateService templateService;

    Server server2Va = Server.refByDescription(US_EAST_STERLING, "sr-va2");

    public SearchQueriesSampleApp() {
        ClcSdk sdk = new ClcSdk(
            new PropertiesFileCredentialsProvider("centurylink-clc-sdk-uat.properties")
        );

        serverService = sdk.serverService();
        groupService = sdk.groupService();
        templateService = sdk.templateService();
    }

    @BeforeClass(groups = SAMPLES)
    public void init() {
        clearAll();

        groupService
            .defineInfrastructure(
                dataCenter(DE_FRANKFURT).subitems(
                    group("uat1", "uat1 group description").subitems(
                        centOsServer("sr-de1")
                    ),
                    group("uat2", "uat2 group description").subitems(
                        centOsServer("sr-de2")
                    )
                ),

                dataCenter(US_EAST_STERLING).subitems(
                    group("uat1", "uat1 group description").subitems(
                        centOsServer("sr-va1"),
                        centOsServer("sr-va2")
                    )
                )
            )

            .waitUntilComplete();


        serverService
            .archive(server2Va)
            .waitUntilComplete();

        assertThatServerHasStatus(server2Va, "archived");
    }

    @AfterClass(groups = SAMPLES)
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

    private void clearAll() {
        serverService
            .delete(new ServerFilter()
                .dataCenters(DE_FRANKFURT, CA_VANCOUVER)
                .status(ACTIVE, ARCHIVED)
            )
            .waitUntilComplete();

        groupService
            .delete(new GroupFilter().nameContains("uat"))
            .waitUntilComplete();
    }

    private ServerMetadata loadServerMetadata(Server server) {
        ServerMetadata metadata = serverService.findByRef(server);
        assertNotNull(metadata);

        return metadata;
    }

    private void assertThatServerHasStatus(Server server, String status) {
        assertEquals(loadServerMetadata(server).getStatus(), status);
    }

    private void checkServerMetadataList(List<ServerMetadata> serverMetadataList,
                                         String... expectedServerDescs) {
        assertNotNull(serverMetadataList);
        assertEquals(serverMetadataList.size(), expectedServerDescs.length);

        assertEquals(
            newHashSet(map(serverMetadataList, ServerMetadata::getDescription)),
            newHashSet(expectedServerDescs)
        );
    }

    private void checkGroupMetadataList(List<GroupMetadata> groupMetadataList,
                                        String... expectedGroupNames) {
        assertNotNull(groupMetadataList);
        assertEquals(groupMetadataList.size(), expectedGroupNames.length);

        assertEquals(
            newHashSet(map(groupMetadataList, GroupMetadata::getName)),
            newHashSet(expectedGroupNames)
        );
    }

    /**
     * Step 1. List all servers available for current user
     */
    @Test(groups = SAMPLES)
    public void findAllServersTest() {
        List<ServerMetadata> serverMetadataList = serverService.find(
            new ServerFilter()
                .dataCenters(DE_FRANKFURT, CA_VANCOUVER)
                .groupNames(DEFAULT_GROUP)
        );

        checkServerMetadataList(serverMetadataList, "sr-de1", "sr-de2", "sr-va1", "sr-va2");
    }

    /**
     * Step 2. Find all active servers in all datacenters
     */
    @Test(groups = SAMPLES)
    public void findAllActiveServersTest() {
        List<ServerMetadata> serverMetadataList = serverService.find(
            new ServerFilter().onlyActive()
        );

        checkServerMetadataList(serverMetadataList, "sr-de1", "sr-de2", "sr-va1");
    }

    /**
     * Step 3. Find server within "uta1" group in all datacenters
     */
    @Test(groups = SAMPLES)
    public void findGroupServersTest() {
        List<ServerMetadata> serverMetadataList = serverService.find(
            new ServerFilter().groupNameContains("uat1")
        );

        checkServerMetadataList(serverMetadataList, "sr-de1", "sr-va1");
    }

    /**
     * Step 4. Find server that contains some value in it's metadata
     */
    @Test(groups = SAMPLES)
    public void findServersByMetadataValueTest() {
        List<ServerMetadata> serverMetadataList = serverService.find(
            new ServerFilter().where(
                serverMetadata -> serverMetadata.getName().toLowerCase().contains("sr-de")
            )
        );

        checkServerMetadataList(serverMetadataList, "sr-de1", "sr-de2");
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
        List<GroupMetadata> groupMetadataList = groupService.find(
            new GroupFilter()
                .dataCenters(DE_FRANKFURT)
                .where(
                    groupMetadata -> groupMetadata.getDescription().contains("uat1")
                )
        );

        checkGroupMetadataList(groupMetadataList, "uat1");
    }
}

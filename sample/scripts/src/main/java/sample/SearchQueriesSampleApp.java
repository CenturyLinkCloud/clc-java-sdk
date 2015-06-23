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
import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.deployment.capabilities.TemplateMetadata;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.DefaultCredentialsProvider;
import com.centurylink.cloud.sdk.server.services.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.GroupService;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.TemplateService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.CreateServerConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.Machine;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.filters.ServerFilter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.refs.Server;
import com.centurylink.cloud.sdk.server.services.dsl.domain.template.filters.TemplateFilter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.template.filters.os.OsFilter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.template.refs.Template;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.ZonedDateTime;
import java.util.List;

import static com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter.DE_FRANKFURT;
import static com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter.US_CENTRAL_SALT_LAKE_CITY;
import static com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter.US_WEST_SANTA_CLARA;
import static com.centurylink.cloud.sdk.core.function.Streams.map;
import static com.centurylink.cloud.sdk.server.services.dsl.domain.InfrastructureConfig.dataCenter;
import static com.centurylink.cloud.sdk.server.services.dsl.domain.group.GroupHierarchyConfig.group;
import static com.centurylink.cloud.sdk.server.services.dsl.domain.server.ServerStatus.ACTIVE;
import static com.centurylink.cloud.sdk.server.services.dsl.domain.server.ServerStatus.ARCHIVED;
import static com.centurylink.cloud.sdk.server.services.dsl.domain.server.ServerType.STANDARD;
import static com.centurylink.cloud.sdk.server.services.dsl.domain.template.filters.os.CpuArchitecture.x86_64;
import static com.centurylink.cloud.sdk.server.services.dsl.domain.template.filters.os.OsType.CENTOS;
import static com.google.common.collect.Sets.newHashSet;
import static sample.SamplesTestsConstants.SAMPLES;

public class SearchQueriesSampleApp extends Assert {

    private ServerService serverService;
    private GroupService groupService;
    private TemplateService templateService;

    private final String group1Name = "uat1";
    private final String group2Name = "uat2";

    private final String serverDe1Name = "sr-de1";
    private final String serverDe2Name = "sr-de2";
    private final String serverVa1Name = "sr-va1";
    private final String serverVa2Name = "sr-va2";

    Server server2Va = Server.refByDescription(US_WEST_SANTA_CLARA, serverVa2Name);

    public SearchQueriesSampleApp() {
        ClcSdk sdk = new ClcSdk(
            new DefaultCredentialsProvider()
        );

        serverService = sdk.serverService();
        groupService = sdk.groupService();
        templateService = sdk.templateService();
    }

    @BeforeClass(groups = {SAMPLES})
    public void init() {
        clearAll();

        groupService
            .defineInfrastructure(
                dataCenter(DE_FRANKFURT).subitems(
                    group(
                        group1Name,
                        "uat1 group description"
                    ).subitems(
                        centOsServer(serverDe1Name)
                    ),
                    group(
                        group2Name,
                        "uat2 group description"
                    )
                    .subitems(
                        centOsServer(serverDe2Name)
                    )
                ),

                dataCenter(US_WEST_SANTA_CLARA).subitems(
                    group(group1Name,
                          "uat1 group description").subitems(
                        centOsServer(serverVa1Name),
                        centOsServer(serverVa2Name)
                    )
                )
            )

            .waitUntilComplete();


        serverService
            .archive(server2Va)
            .waitUntilComplete();

        assertThatServerHasStatus(server2Va, "archived");
    }

    @AfterClass(groups = {SAMPLES})
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
                .dataCenters(DE_FRANKFURT, US_WEST_SANTA_CLARA)
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
    @Test(groups = {SAMPLES})
    public void findAllServersTest() {
        List<ServerMetadata> serverMetadataList = serverService.find(
            new ServerFilter()
                .status(ACTIVE, ARCHIVED)
        );

        checkServerMetadataList(serverMetadataList, serverDe1Name, serverDe2Name, serverVa1Name, serverVa2Name);
    }

    /**
     * Step 2. Find all active servers in all datacenters
     */
    @Test(groups = {SAMPLES})
    public void findAllActiveServersTest() {
        List<ServerMetadata> serverMetadataList = serverService.find(
            new ServerFilter().onlyActive()
        );

        checkServerMetadataList(serverMetadataList, serverDe1Name, serverDe2Name, serverVa1Name);
    }

    /**
     * Step 3. Find server within "uta1" group in all datacenters
     */
    @Test(groups = {SAMPLES})
    public void findGroupServersTest() {
        List<ServerMetadata> serverMetadataList = serverService.find(
            new ServerFilter().groupNameContains(group1Name)
        );

        checkServerMetadataList(serverMetadataList, serverDe1Name, serverVa1Name);
    }

    /**
     * Step 4. Find server that contains some value in it's metadata
     */
    @Test(groups = {SAMPLES})
    public void findServersByMetadataValueTest() {
        List<ServerMetadata> serverMetadataList = serverService.find(
            new ServerFilter().where(
                serverMetadata -> serverMetadata.getName().toLowerCase().contains("sr-de")
            )
        );

        checkServerMetadataList(serverMetadataList, serverDe1Name, serverDe2Name);
    }

    /**
     * Step 5. Find templates of specified operating system
     */
    @Test(groups = {SAMPLES})
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
    @Test(groups = {SAMPLES})
    public void testFindGroupsByDescriptionKeyword() {
        List<GroupMetadata> groupMetadataList = groupService.find(
            new GroupFilter()
                .dataCenters(DE_FRANKFURT)
                .where(groupMetadata ->
                    groupMetadata.getDescription() != null && groupMetadata.getDescription().contains(group1Name)
                )
        );

        checkGroupMetadataList(groupMetadataList, group1Name);
    }
}

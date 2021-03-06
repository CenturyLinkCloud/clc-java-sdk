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

package com.centurylink.cloud.sdk.server.services.dsl.groups;

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.injector.Inject;
import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.GroupService;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.InfrastructureConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.GroupHierarchyConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter.CA_TORONTO_1;
import static com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter.US_CENTRAL_SALT_LAKE_CITY;
import static com.centurylink.cloud.sdk.core.function.Predicates.notNull;
import static com.centurylink.cloud.sdk.server.services.SampleServerConfigs.apacheHttpServer;
import static com.centurylink.cloud.sdk.server.services.SampleServerConfigs.mysqlServer;
import static com.centurylink.cloud.sdk.server.services.dsl.domain.group.GroupHierarchyConfig.group;
import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;
import static java.util.stream.Collectors.toList;

/**
 * @author Aliaksandr Krasitski
 */
@Test(groups = {RECORDED})
public class InfrastructureTest extends AbstractServersSdkTest implements WireMockMixin {

    @Inject
    GroupService groupService;

    @Inject
    ServerService serverService;

    private String name(String value) {
        return value;
    }

    @Test
    @WireMockFileSource("infrastructure")
    public void testInfrastructure() throws Exception {
        checkInfrastructure(
            initConfig(
                CA_TORONTO_1,
                US_CENTRAL_SALT_LAKE_CITY
            )
        );
    }

    private List<GroupMetadata> checkInfrastructure(InfrastructureConfig... configs) {
        List<GroupMetadata> groups = defineInfrastructure(configs);

        Arrays.asList(configs).stream()
            .forEach(config ->
                groups.stream()
                    .forEach(group ->
                        config.getSubitems().stream()
                            .forEach(cfg -> checkGroup(group, cfg))
                    )
            );
        return groups;
    }

    private InfrastructureConfig initConfig(DataCenter... dataCenters) {
        return new InfrastructureConfig()
            .dataCenters(dataCenters)
            .subitems(new GroupHierarchyConfig()
                .name(name("Parent Group"))
                .subitems(
                    group(name("Group1-1")).subitems(
                        group(name("Group1-1-1")).subitems(
                            mysqlServer().count(2)
                        ),
                        group(name("Group1-1-2")).subitems(
                            group(name("Group1-1-2-1")),
                            apacheHttpServer()
                        )
                    ),
                    group(name("Group1-2"))
                ));
    }

    private List<GroupMetadata> defineInfrastructure(InfrastructureConfig... config) {
        List<Group> groups = groupService.defineInfrastructure(config).waitUntilComplete().getResult();

        return groupService.find(new GroupFilter().groups(groups.toArray(new Group[groups.size()])));
    }

    private void checkGroup(GroupMetadata groupMetadata, GroupHierarchyConfig hierarchyConfig) {
        if (hierarchyConfig == null) {
            return;
        }
        List<String> metadataNames = groupMetadata.getGroups().stream()
            .map(GroupMetadata::getName)
            .collect(toList());

        List<String> configNames = hierarchyConfig.getSubitems().stream()
            .filter(cfg -> cfg instanceof GroupHierarchyConfig)
            .map(cfg -> ((GroupHierarchyConfig) cfg).getName())
            .filter(notNull())
            .collect(toList());

        assertTrue(metadataNames.containsAll(configNames), "group must have all groups from config");

        groupMetadata.getGroups().stream()
            .forEach(group -> {
                GroupHierarchyConfig nextConfig = (GroupHierarchyConfig)hierarchyConfig.getSubitems().stream()
                    .filter(cfg -> cfg instanceof GroupHierarchyConfig)
                    .filter(subgroup -> ((GroupHierarchyConfig)subgroup).getName().equals(group.getName()))
                    .findFirst()
                    .orElse(null);
                checkGroup(group, nextConfig);
            });
    }
}

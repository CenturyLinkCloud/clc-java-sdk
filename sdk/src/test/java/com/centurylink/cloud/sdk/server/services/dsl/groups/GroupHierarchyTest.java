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

import com.centurylink.cloud.sdk.core.injector.Inject;
import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.GroupService;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.GroupHierarchyConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.filters.ServerFilter;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter.DE_FRANKFURT;
import static com.centurylink.cloud.sdk.core.function.Predicates.notNull;
import static com.centurylink.cloud.sdk.server.services.SampleServerConfigs.*;
import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;
import static java.util.stream.Collectors.toList;

/**
 * @author Aliaksandr Krasitski
 */
public class GroupHierarchyTest extends AbstractServersSdkTest {

    @Inject
    ServerService serverService;

    @Inject
    GroupService groupService;

    @Test(enabled = false, groups = {INTEGRATION, LONG_RUNNING})
    public void testGroupHierarchy() {
        GroupHierarchyConfig config = initConfig();
        GroupMetadata parentGroup = defineGroupHierarchy(config);

        checkGroup(parentGroup, config);

        deleteGroup(parentGroup);
    }

    private GroupHierarchyConfig initConfig() {
        return new GroupHierarchyConfig()
            .name("Parent Group")
            .subitems(
                GroupHierarchyConfig.group("Group1-1").subitems(
                    GroupHierarchyConfig.group("Group1-1-1").subitems(
                        mysqlServer().count(2),
                        apacheHttpServer()
                    ),
                    GroupHierarchyConfig.group("Group1-1-2").subitems(
                        GroupHierarchyConfig.group("Group1-1-2-1"),
                        nginxServer()
                    )
                ),

                GroupHierarchyConfig.group("Group1-2")
            );
    }

    private GroupMetadata defineGroupHierarchy(GroupHierarchyConfig config) {
        groupService.defineGroupHierarchy(DE_FRANKFURT, config).waitUntilComplete();

        return
            groupService.find(
                new GroupFilter()
                    .dataCenters(DE_FRANKFURT)
                    .nameContains(config.getName())
            )
            .stream()
            .findFirst()
            .get();
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

    private void deleteGroup(GroupMetadata groupMetadata) {
        serverService
            .delete(new ServerFilter()
                .dataCenters(DE_FRANKFURT)
                .groupNames("Group1-1-1", "Group1-1-2")
            )
            .waitUntilComplete();

        groupService
            .delete(Group.refById(groupMetadata.getId()))
            .waitUntilComplete();
    }
}

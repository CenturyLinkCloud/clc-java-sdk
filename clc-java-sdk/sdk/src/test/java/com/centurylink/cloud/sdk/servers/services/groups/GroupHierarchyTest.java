package com.centurylink.cloud.sdk.servers.services.groups;

import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.SampleServerConfigs;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.group.GroupHierarchyConfig;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter.DE_FRANKFURT;
import static com.centurylink.cloud.sdk.core.function.Predicates.notNull;
import static com.centurylink.cloud.sdk.servers.SampleServerConfigs.apacheHttpServer;
import static com.centurylink.cloud.sdk.servers.SampleServerConfigs.mysqlServer;
import static com.centurylink.cloud.sdk.servers.SampleServerConfigs.nginxServer;
import static com.centurylink.cloud.sdk.servers.services.domain.group.GroupHierarchyConfig.group;
import static com.centurylink.cloud.sdk.servers.services.domain.server.CreateServerConfig.*;
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

    @Test(groups = {INTEGRATION, LONG_RUNNING})
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
                group("Group1-1").subitems(
                    group("Group1-1-1").subitems(
                        mysqlServer().count(2),
                        apacheHttpServer()
                    ),
                    group("Group1-1-2").subitems(
                        group("Group1-1-2-1"),
                        nginxServer()
                    )
                ),

                group("Group1-2")
            );
    }

    private GroupMetadata defineGroupHierarchy(GroupHierarchyConfig config) {
        groupService.defineGroupHierarchy(DE_FRANKFURT, config).waitUntilComplete();

        return groupService.findByDataCenter(DE_FRANKFURT).stream()
            .filter(group -> group.getName().equals(config.getName()))
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

        //TODO GroupMetadata.getServers() returns empty list. should it be filled?
//        assertEquals(groupMetadata.getServers().size(), hierarchyConfig.getServers().size(),
//            "check server count"
//        );

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

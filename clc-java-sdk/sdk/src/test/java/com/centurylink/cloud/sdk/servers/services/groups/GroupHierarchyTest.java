package com.centurylink.cloud.sdk.servers.services.groups;

import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.domain.group.GroupHierarchyConfig;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.servers.services.domain.group.GroupHierarchyConfig.group;
import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;
import static java.util.stream.Collectors.toList;

/**
 * @author Aliaksandr Krasitski
 */
public class GroupHierarchyTest extends AbstractServersSdkTest {

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
            .subgroups(
                group("Group1-1").subgroups(
                    group("Group1-1-1"),
                    group("Group1-1-2")
                        .subgroups(
                            group("Group1-1-2-1")
                        )
                ),
                group("Group1-2")
            );
    }

    private GroupMetadata defineGroupHierarchy(GroupHierarchyConfig config) {
        groupService.defineGroupHierarchy(DataCenter.DE_FRANKFURT, config);

        return groupService.findByDataCenter(DataCenter.DE_FRANKFURT).stream()
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

        List<String> configNames = hierarchyConfig.getSubgroups().stream()
            .map(GroupHierarchyConfig::getName)
            .collect(toList());

        assertTrue(metadataNames.containsAll(configNames), "group must have all groups from config");

        groupMetadata.getGroups().stream()
            .forEach(group -> {
                GroupHierarchyConfig nextConfig = hierarchyConfig.getSubgroups().stream()
                    .filter(subgroup -> subgroup.getName().equals(group.getName()))
                    .findFirst()
                    .orElse(null);
                checkGroup(group, nextConfig);
            });
    }

    private void deleteGroup(GroupMetadata groupMetadata) {
        groupService.delete(Group.refById(groupMetadata.getId())).waitUntilComplete();
    }
}

package com.centurylink.cloud.sdk.servers.services.groups;

import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.domain.group.GroupHierarchyConfig;
import com.centurylink.cloud.sdk.servers.services.domain.group.InfrastructureConfig;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.GroupByIdRef;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static com.centurylink.cloud.sdk.core.function.Predicates.notNull;
import static com.centurylink.cloud.sdk.servers.services.domain.group.GroupHierarchyConfig.group;
import static com.centurylink.cloud.sdk.servers.services.domain.server.CreateServerConfig.*;
import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;
import static java.util.stream.Collectors.toList;

/**
 * @author Aliaksandr Krasitski
 */
public class InfrastructureTest extends AbstractServersSdkTest {

    @Inject
    GroupService groupService;

    @Test(groups = {INTEGRATION, LONG_RUNNING})
    public void testInfrastructure() {

        List<GroupMetadata> groups = checkInfrastructure(
            initConfig(
                DataCenter.CA_TORONTO_1,
                DataCenter.US_CENTRAL_SALT_LAKE_CITY
            )
        );

        deleteGroups(groups);
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
            .datacenter(dataCenters)
            .subitems(new GroupHierarchyConfig()
                .name("Parent Group")
                .subitems(
                    group("Group1-1").subitems(
                        group("Group1-1-1")
                            .subitems(
                                mysqlServer().count(2),
                                apacheHttpServer()),
                        group("Group1-1-2")
                            .subitems(
                                group("Group1-1-2-1"),
                                nginxServer()
                            )
                    ),
                    group("Group1-2")
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

    private void deleteGroups(List<GroupMetadata> groups) {
        List<GroupByIdRef> refs = groups.stream()
            .map(groupMetadata -> Group.refById(groupMetadata.getId()))
            .collect(toList());
        groupService.delete(refs.toArray(new Group[refs.size()])).waitUntilComplete();
    }
}

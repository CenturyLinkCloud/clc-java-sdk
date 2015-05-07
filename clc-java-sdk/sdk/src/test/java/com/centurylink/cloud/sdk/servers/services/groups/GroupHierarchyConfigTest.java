package com.centurylink.cloud.sdk.servers.services.groups;

import com.centurylink.cloud.sdk.servers.services.domain.group.GroupHierarchyConfig;
import com.centurylink.cloud.sdk.servers.services.domain.server.ServerConfig;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.servers.services.domain.group.GroupHierarchyConfig.group;
import static com.centurylink.cloud.sdk.servers.services.domain.server.CreateServerConfig.*;
import static java.util.stream.Collectors.summingInt;
import static org.testng.Assert.assertEquals;

/**
 * @author Aliaksandr Krasitski
 */
public class GroupHierarchyConfigTest {

    GroupHierarchyConfig groupHierarchyConfig;

    @Test
    public void testGroupHierarchyConfig() {
        initConfig();
        checkConfig();
    }

    private void initConfig() {
        groupHierarchyConfig = new GroupHierarchyConfig()
            .name("Parent Group")
            .subitems(
                group("Group1-1").subitems(
                    group("Group1-1-1")
                        .subitems(
                            mysqlServer().count(2),
                            apacheHttpServer().count(5)),
                    group("Group1-1-2")
                        .subitems(
                            group("Group1-1-2-1"),
                            nginxServer()
                        )
                ),
                group("Group1-2")
            );
    }

    private void checkConfig() {
        int serversCount = getServersCountInGroup(groupHierarchyConfig);
        assertEquals(serversCount, groupHierarchyConfig.getServerConfigs().size());
    }

    private int getServersCountInGroup(GroupHierarchyConfig config) {
        return getServersCount(config) +
            config.getSubitems().stream()
                .filter(cfg -> cfg instanceof GroupHierarchyConfig)
                .collect(summingInt(cfg -> getServersCountInGroup((GroupHierarchyConfig) cfg)));
    }

    private int getServersCount(GroupHierarchyConfig config) {
        return config.getSubitems().stream()
            .filter(cfg -> cfg instanceof ServerConfig)
            .collect(summingInt(cfg -> ((ServerConfig)cfg).getServerConfig().length));
    }

}

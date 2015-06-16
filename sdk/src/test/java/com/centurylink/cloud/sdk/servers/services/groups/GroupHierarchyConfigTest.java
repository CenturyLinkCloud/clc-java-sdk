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

package com.centurylink.cloud.sdk.servers.services.groups;

import com.centurylink.cloud.sdk.servers.services.domain.group.GroupHierarchyConfig;
import com.centurylink.cloud.sdk.servers.services.domain.server.ServerConfig;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.servers.SampleServerConfigs.apacheHttpServer;
import static com.centurylink.cloud.sdk.servers.SampleServerConfigs.mysqlServer;
import static com.centurylink.cloud.sdk.servers.SampleServerConfigs.nginxServer;
import static com.centurylink.cloud.sdk.servers.services.domain.group.GroupHierarchyConfig.group;
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
                    group("Group1-1-1").subitems(
                        mysqlServer().count(2),
                        apacheHttpServer().count(5)
                    ),
                    group("Group1-1-2").subitems(
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

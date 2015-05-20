/*
 * (c) 2015 CenturyLink Cloud. All Rights Reserved.
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

package com.centurylink.cloud.sdk.servers.services.domain.group;

import java.util.ArrayList;
import java.util.List;

public class GroupBilling {

    private String groupId;
    private String name;
    private List<ServerBilling> servers = new ArrayList<>();

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public GroupBilling groupId(String groupId) {
        setGroupId(groupId);
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GroupBilling name(String name) {
        setName(name);
        return this;
    }

    public List<ServerBilling> getServers() {
        return servers;
    }

    public void setServers(List<ServerBilling> servers) {
        this.servers = servers;
    }

    public GroupBilling server(ServerBilling server) {
        this.servers.add(server);
        return this;
    }

    public GroupBilling servers(List<ServerBilling> servers) {
        setServers(servers);
        return this;
    }
}

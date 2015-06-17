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

package com.centurylink.cloud.sdk.servers;

import com.centurylink.cloud.sdk.common.management.CommonManagementModule;
import com.centurylink.cloud.sdk.networks.NetworksModule;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.TemplateService;
import com.centurylink.cloud.sdk.servers.services.domain.group.GroupConverter;
import com.centurylink.cloud.sdk.servers.services.domain.server.ServerConverter;
import com.google.inject.AbstractModule;

/**
 * @author ilya.drabenia
 */
public class ServersModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ServerClient.class);

        bind(ServerService.class);
        bind(ServerConverter.class);
        bind(GroupConverter.class);
        bind(GroupService.class);
        bind(TemplateService.class);

        install(new CommonManagementModule());
        install(new NetworksModule());
    }

}

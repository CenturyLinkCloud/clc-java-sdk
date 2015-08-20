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

package com.centurylink.cloud.sdk.server.services;

import com.centurylink.cloud.sdk.base.services.BaseModule;
import com.centurylink.cloud.sdk.network.services.NetworkModule;
import com.centurylink.cloud.sdk.server.services.client.ServerClient;
import com.centurylink.cloud.sdk.server.services.dsl.GroupService;
import com.centurylink.cloud.sdk.server.services.dsl.InvoiceService;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.TemplateService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.ServerConverter;
import com.google.inject.AbstractModule;

/**
 * @author ilya.drabenia
 */
public class ServerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ServerClient.class);

        bind(ServerService.class);
        bind(ServerConverter.class);
        bind(GroupService.class);
        bind(TemplateService.class);

        bind(InvoiceService.class);

        install(new BaseModule());
        install(new NetworkModule());
    }

}

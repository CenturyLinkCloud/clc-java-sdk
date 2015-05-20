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

package com.centurylink.cloud.sdk;

import com.centurylink.cloud.sdk.common.management.services.DataCenterService;
import com.centurylink.cloud.sdk.core.auth.AuthModule;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.CredentialsProvider;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.PropertiesFileCredentialsProvider;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.StaticCredentialsProvider;
import com.centurylink.cloud.sdk.core.config.SdkConfiguration;
import com.centurylink.cloud.sdk.servers.ServersModule;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.TemplateService;
import com.google.inject.Guice;
import com.google.inject.Inject;

/**
 * @author ilya.drabenia
 */
// TODO: 1. need to implement support of integration with Spring
// TODO: 2. need to add network service to this root object (network service delayed)
public class ClcSdk {

    @Inject
    ServerService serverService;

    @Inject
    GroupService groupService;

    @Inject
    TemplateService templateService;

    @Inject
    DataCenterService dataCenterService;

    @Inject
    CredentialsProvider credentialsProvider;

    public ClcSdk() {
        this(new PropertiesFileCredentialsProvider());
    }

    public ClcSdk(CredentialsProvider credentialsProvider) {
        this(credentialsProvider, SdkConfiguration.builder().build());
    }

    public ClcSdk(CredentialsProvider credentialsProvider, SdkConfiguration config) {
        Guice
            .createInjector(
                config.asModule(),
                new AuthModule(credentialsProvider),
                new ServersModule()
            )
            .injectMembers(this);
    }

    public ServerService serverService() {
        return serverService;
    }

    public GroupService groupService() {
        return groupService;
    }

    public TemplateService templateService() {
        return templateService;
    }

    public DataCenterService dataCenterService() {
        return dataCenterService;
    }

    public CredentialsProvider getCredentialsProvider() {
        return credentialsProvider;
    }
}

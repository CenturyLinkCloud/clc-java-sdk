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

package com.centurylink.cloud.sdk.core.client;

import com.centurylink.cloud.sdk.core.auth.services.BearerAuthentication;
import com.centurylink.cloud.sdk.core.config.SdkConfiguration;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;

import javax.ws.rs.client.WebTarget;

/**
 * @author aliaksandr.krasitski
 */
public class AuthenticatedSdkHttpClient extends SdkHttpClient {

    protected final BearerAuthentication authentication;

    public AuthenticatedSdkHttpClient(BearerAuthentication authFilter, SdkConfiguration config) {
        super(config);
        this.authentication = authFilter;
    }

    protected WebTarget client(String targetUrl) {
        return
            buildWebTarget(
                buildClient(),
                CLC_API_URL + targetUrl
            );
    }

    protected WebTarget experimentalClient(String targetUrl) {
        return
            buildWebTarget(
                buildClient(),
                CLC_API_URL_EXPERIMENTAL + targetUrl
            );
    }

    @Override
    protected ResteasyClient buildClient() {
        return
            super.buildClient()
                .register(authentication)
                .register(new UserAgentRequestFilter());
    }

    private WebTarget buildWebTarget(ResteasyClient resteasyClient, String url) {
        return
            resteasyClient
                .target(url)
                .resolveTemplate("accountAlias", authentication.getAccountAlias());
    }
}

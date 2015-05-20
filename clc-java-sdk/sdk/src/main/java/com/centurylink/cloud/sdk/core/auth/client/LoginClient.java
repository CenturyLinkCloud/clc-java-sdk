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

package com.centurylink.cloud.sdk.core.auth.client;

import com.centurylink.cloud.sdk.core.auth.client.domain.LoginRequest;
import com.centurylink.cloud.sdk.core.auth.client.domain.LoginResponse;
import com.centurylink.cloud.sdk.core.client.SdkHttpClient;
import com.centurylink.cloud.sdk.core.config.SdkConfiguration;
import com.google.inject.Inject;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

/**
 * @author ilya.drabenia
 */
public class LoginClient extends SdkHttpClient {

    @Inject
    public LoginClient(SdkConfiguration config) {
        super(config);
    }

    public LoginResponse login(LoginRequest credentials) {
        return
            buildClient()
                .target(CLC_API_URL + "/authentication/login")
                .request().post(
                    entity(credentials, APPLICATION_JSON_TYPE)
                )
                .readEntity(LoginResponse.class);
    }

}

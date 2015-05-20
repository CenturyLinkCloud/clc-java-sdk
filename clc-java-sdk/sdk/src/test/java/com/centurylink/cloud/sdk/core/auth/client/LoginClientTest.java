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
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.Credentials;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.PropertiesFileCredentialsProvider;
import com.centurylink.cloud.sdk.core.config.SdkConfiguration;
import org.testng.annotations.Test;

import javax.ws.rs.client.ResponseProcessingException;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;

@Test(groups = INTEGRATION)
public class LoginClientTest {

    LoginClient client = new LoginClient(SdkConfiguration.DEFAULT);

    @Test
    public void testLoginWithCorrectCredentials() {
        Credentials credentials = new PropertiesFileCredentialsProvider().getCredentials();

        LoginResponse response = client.login(
            new LoginRequest(credentials.getUsername(), credentials.getPassword())
        );

        assert response.getAccountAlias() != null;
        assert response.getBearerToken() != null;
    }

    @Test(expectedExceptions = ResponseProcessingException.class)
    public void testLoginWithIncorrectCredentials() {
        client.login(new LoginRequest("incorrect", "account"));
    }

}
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

package com.centurylink.cloud.sdk.core.auth.services;

import com.centurylink.cloud.sdk.core.auth.client.LoginClient;
import com.centurylink.cloud.sdk.core.auth.client.domain.LoginResponse;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.DefaultCredentialsProvider;
import com.centurylink.cloud.sdk.tests.TestGroups;
import org.testng.annotations.Test;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.core.MultivaluedHashMap;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BearerAuthenticationTest {

    BearerAuthentication auth = new BearerAuthentication(
        new DefaultCredentialsProvider(),
        mock(LoginClient.class)
    );

    private ClientRequestContext stubRequestContext() {
        ClientRequestContext context = mock(ClientRequestContext.class);

        when(context.getHeaders()).thenReturn(new MultivaluedHashMap<>());

        return context;
    }

    @Test(groups = INTEGRATION)
    public void testBearerTokenFilter() throws Exception {
        mockLogin();
        ClientRequestContext context = stubRequestContext();

        auth.filter(context);

        assert context.getHeaders().get("Authorization") != null;
    }

    private void mockLogin() {
        when(auth.getLoginClient().login(any())).thenReturn(
            new LoginResponse("idrabenia", "ALTR", "VA1", null, "BeArERtOkeN")
        );
    }

}
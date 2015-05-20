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

package com.centurylink.cloud.sdk.core.auth.services;

import com.centurylink.cloud.sdk.core.auth.client.LoginClient;
import com.centurylink.cloud.sdk.core.auth.client.domain.LoginRequest;
import com.centurylink.cloud.sdk.core.auth.client.domain.LoginResponse;
import com.centurylink.cloud.sdk.core.auth.services.domain.AuthToken;
import com.centurylink.cloud.sdk.core.auth.services.domain.SessionCredentials;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.Credentials;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.CredentialsProvider;
import com.google.inject.Inject;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import java.io.IOException;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;

/**
 * @author ilya.drabenia
 */
public class BearerAuthentication implements ClientRequestFilter {
    private final CredentialsProvider credentialsProvider;
    private final LoginClient client;

    private volatile SessionCredentials session;

    @Inject
    public BearerAuthentication(CredentialsProvider credentialsProvider, LoginClient client) {
        this.credentialsProvider = credentialsProvider;
        this.client = client;
    }

    public LoginClient getLoginClient() {
        return client;
    }

    private void requestSessionCredentialsIfNeeded() {
        if (session == null
                || !session.getCredentials().isEqualTo(curCredentials())
                || session.getToken().isExpired()) {
            session = requestNewSessionCredentials();
        }
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        requestSessionCredentialsIfNeeded();

        requestContext
            .getHeaders()
            .putSingle(AUTHORIZATION, session.getToken().toHeaderString());
    }

    private SessionCredentials requestNewSessionCredentials() {
        Credentials credentials = curCredentials();

        LoginResponse result = client.login(new LoginRequest(
            credentials.getUsername(),
            credentials.getPassword()
        ));

        return new SessionCredentials(
            new AuthToken(result.getBearerToken()),
            result.getAccountAlias(),
            credentials
        );
    }

    private Credentials curCredentials() {
        return
            credentialsProvider
                .refresh()
                .getCredentials();
    }

    public String getAccountAlias() {
        requestSessionCredentialsIfNeeded();

        return session.getAccountAlias();
    }
}

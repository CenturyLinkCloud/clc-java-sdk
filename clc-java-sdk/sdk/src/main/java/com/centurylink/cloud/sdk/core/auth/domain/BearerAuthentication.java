package com.centurylink.cloud.sdk.core.auth.domain;

import com.centurylink.cloud.sdk.core.auth.client.LoginClient;
import com.centurylink.cloud.sdk.core.auth.client.domain.LoginRequest;
import com.centurylink.cloud.sdk.core.auth.client.domain.LoginResponse;
import com.centurylink.cloud.sdk.core.auth.domain.credentials.Credentials;
import com.centurylink.cloud.sdk.core.auth.domain.credentials.CredentialsProvider;
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

        requestSessionCredentialsIfNeeded();
    }

    private void requestSessionCredentialsIfNeeded() {
        if (session == null || session.getToken().isExpired()) {
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
        LoginResponse result = client.login(new LoginRequest(
            credentials().getUsername(),
            credentials().getPassword()
        ));

        return new SessionCredentials(
            new AuthToken(result.getBearerToken()),
            result.getAccountAlias()
        );
    }

    private Credentials credentials() {
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

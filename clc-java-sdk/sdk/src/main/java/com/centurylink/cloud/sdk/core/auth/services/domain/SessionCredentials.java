package com.centurylink.cloud.sdk.core.auth.services.domain;

import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.Credentials;

/**
 * @author ilya.drabenia
 */
public class SessionCredentials {
    private final AuthToken token;
    private final String accountAlias;
    private final Credentials credentials;

    public SessionCredentials(AuthToken token, String accountAlias, Credentials credentials) {
        this.token = token;
        this.accountAlias = accountAlias;
        this.credentials = credentials;
    }

    public AuthToken getToken() {
        return token;
    }

    public String getAccountAlias() {
        return accountAlias;
    }

    public Credentials getCredentials() {
        return credentials;
    }
}

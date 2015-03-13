package com.centurylink.cloud.sdk.core.auth.services.domain;

/**
 * @author ilya.drabenia
 */
public class SessionCredentials {
    private final AuthToken token;
    private final String accountAlias;

    public SessionCredentials(AuthToken token, String accountAlias) {
        this.token = token;
        this.accountAlias = accountAlias;
    }

    public AuthToken getToken() {
        return token;
    }

    public String getAccountAlias() {
        return accountAlias;
    }
}

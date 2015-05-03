package com.centurylink.cloud.sdk.base.auth.services.domain.credentials;

/**
 * @author ilya.drabenia
 */
public class StaticCredentialsProvider implements CredentialsProvider {
    private final Credentials credentials;

    public StaticCredentialsProvider(String username, String password) {
        this.credentials = new Credentials(username, password);
    }

    public StaticCredentialsProvider(Credentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public Credentials getCredentials() {
        return credentials;
    }

    @Override
    public CredentialsProvider refresh() {
        return this;
    }
}

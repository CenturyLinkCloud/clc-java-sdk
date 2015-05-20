package com.centurylink.cloud.sdk.core.auth.services.domain.credentials;

/**
 * Class that provide possibilities to receive credentials from Environment variables.
 * Default property key for username is <b>CLC_USERNAME</b> and for password is <b>CLC_PASSWORD</b>.
 */
public class EnvironmentVariableCredentialsProvider implements CredentialsProvider {
    private final String usernameKey;
    private final String passwordKey;

    private volatile Credentials credentials;

    public EnvironmentVariableCredentialsProvider() {
        this("CLC_USERNAME", "CLC_PASSWORD");
    }

    public EnvironmentVariableCredentialsProvider(String usernameKey, String passwordKey) {
        this.usernameKey = usernameKey;
        this.passwordKey = passwordKey;

        refresh();
    }

    @Override
    public Credentials getCredentials() {
        return credentials;
    }

    @Override
    public CredentialsProvider refresh() {
        credentials = new Credentials(
            System.getenv(usernameKey),
            System.getenv(passwordKey)
        );

        return this;
    }
}

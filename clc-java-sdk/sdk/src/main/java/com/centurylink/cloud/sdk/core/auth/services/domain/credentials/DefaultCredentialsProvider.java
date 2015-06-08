package com.centurylink.cloud.sdk.core.auth.services.domain.credentials;

import com.centurylink.cloud.sdk.core.exceptions.ClcException;

import static com.google.common.base.Strings.isNullOrEmpty;

public class DefaultCredentialsProvider implements CredentialsProvider {

    private CredentialsProvider credentialsProvider;

    public DefaultCredentialsProvider() {
        if (
            !loadFromSystemVariables(null, null)
            && !loadFromEnvironmentVariables(null, null)
            && !loadFromPropertiesFile(null)
        ) {
            throwFailedException();
        }
    }

    public DefaultCredentialsProvider(String usernameKey, String passwordKey) {
        if (
            !loadFromSystemVariables(usernameKey, passwordKey)
            && !loadFromEnvironmentVariables(usernameKey, passwordKey)
        ) {
            throwFailedException();
        }

        refresh();
    }

    public DefaultCredentialsProvider(String filePath) {
        if (!loadFromPropertiesFile(filePath)) {
            throwFailedException();
        }

        refresh();
    }

    private void throwFailedException() {
        throw new ClcException("Failed to provide credentials");
    }

    private boolean loadFromSystemVariables(String usernameKey, String passwordKey) {
        return checkAndSetCredentials(
            usernameKey != null && passwordKey != null
                ? new SystemVariableCredentialsProvider(usernameKey, passwordKey)
                : new SystemVariableCredentialsProvider()
        );
    }

    private boolean loadFromEnvironmentVariables(String usernameKey, String passwordKey) {
        return checkAndSetCredentials(
            usernameKey != null && passwordKey != null
                ? new EnvironmentVariableCredentialsProvider(usernameKey, passwordKey)
                : new EnvironmentVariableCredentialsProvider()
        );
    }

    private boolean loadFromPropertiesFile(String filePath) {
        return checkAndSetCredentials(
            filePath != null
                ? new PropertiesFileCredentialsProvider(filePath)
                : new PropertiesFileCredentialsProvider()
        );
    }

    private boolean checkAndSetCredentials(CredentialsProvider provider) {
        Credentials credentials = provider.getCredentials();

        if (isNullOrEmpty(credentials.getUsername()) || isNullOrEmpty(credentials.getPassword())) {
            return false;
        }

        credentialsProvider = provider;
        return true;
    }

    @Override
    public Credentials getCredentials() {
        return credentialsProvider.getCredentials();
    }

    @Override
    public CredentialsProvider refresh() {
        credentialsProvider.refresh();
        return this;
    }
}

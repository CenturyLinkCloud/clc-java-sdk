package com.centurylink.cloud.sdk.core.auth.config;

import com.centurylink.cloud.sdk.core.auth.client.LoginClient;
import com.centurylink.cloud.sdk.core.auth.domain.BearerAuthentication;
import com.centurylink.cloud.sdk.core.auth.domain.credentials.CredentialsProvider;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * @author ilya.drabenia
 */
public class AuthModule extends AbstractModule {
    private final CredentialsProvider credentialsProvider;

    public AuthModule(CredentialsProvider credentialsProvider) {
        this.credentialsProvider = credentialsProvider;
    }

    @Override
    protected void configure() {
        bind(LoginClient.class);
        bind(BearerAuthentication.class);
    }

    @Provides
    public CredentialsProvider getCredentialsProvider() {
        return credentialsProvider;
    }
}

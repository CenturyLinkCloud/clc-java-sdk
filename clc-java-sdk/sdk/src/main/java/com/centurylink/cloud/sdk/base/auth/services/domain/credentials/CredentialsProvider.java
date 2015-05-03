package com.centurylink.cloud.sdk.base.auth.services.domain.credentials;

/**
 * @author ilya.drabenia
 */
public interface CredentialsProvider {

    Credentials getCredentials();

    CredentialsProvider refresh();

}

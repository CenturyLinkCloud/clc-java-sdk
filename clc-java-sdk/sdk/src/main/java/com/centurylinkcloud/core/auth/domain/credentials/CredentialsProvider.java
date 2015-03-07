package com.centurylinkcloud.core.auth.domain.credentials;

/**
 * @author ilya.drabenia
 */
public interface CredentialsProvider {

    Credentials getCredentials();

    CredentialsProvider refresh();

}

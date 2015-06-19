package com.centurylink.cloud.sdk.core.auth.services.domain.credentials;

import org.testng.annotations.Test;


/**
 * @author Aliaksandr Krasitski
 */
public class StaticCredentialsProviderTest {

    private String userName = "user";
    private String userPassword = "password";

    @Test
    public void testConstructorWithUserParams() {
        CredentialsProvider provider = new StaticCredentialsProvider(userName, userPassword);
        Credentials credentials = provider.getCredentials();
        provider.refresh();

        assert userName.equals(credentials.getUsername());
        assert userPassword.equals(credentials.getPassword());
    }

    @Test
    public void testConstructorWithCredentials() {
        CredentialsProvider provider = new StaticCredentialsProvider(new Credentials(userName, userPassword));
        Credentials credentials = provider.getCredentials();

        assert userName.equals(credentials.getUsername());
        assert userPassword.equals(credentials.getPassword());
    }

}

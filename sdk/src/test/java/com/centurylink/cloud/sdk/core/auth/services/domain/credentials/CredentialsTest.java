package com.centurylink.cloud.sdk.core.auth.services.domain.credentials;

import org.testng.annotations.Test;


/**
 * @author Aliaksandr Krasitski
 */
public class CredentialsTest {

    private String userName = "user";
    private String userPassword = "password";

    @Test
    public void testCredentials() {
        Credentials credentials = new Credentials(userName, userPassword);

        assert credentials.isEqualTo(new Credentials(userName, userPassword));
        assert userName.equals(credentials.getUsername());
        assert userPassword.equals(credentials.getPassword());
        assert credentials.toString().equals(credentials.toReadableString());
    }

}

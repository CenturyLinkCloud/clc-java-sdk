package com.centurylink.cloud.sdk.core.auth.services.domain.credentials;

import org.testng.annotations.Test;


/**
 * @author Aliaksandr Krasitski
 */
public class CredentialsTest {

    @Test
    public void testCredentials() {
        String userName = "user";
        String userPassword = "passwd";

        Credentials credentials = new Credentials(userName, userPassword);

        assert credentials.isEqualTo(new Credentials(userName, userPassword));
        assert userName.equals(credentials.getUsername());
        assert userPassword.equals(credentials.getPassword());
        assert credentials.toString().equals(credentials.toReadableString());
    }

}

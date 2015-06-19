package com.centurylink.cloud.sdk.core.auth.services.domain.credentials;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Aliaksandr Krasitski
 */
public class SystemVariableCredentialsProviderTest {

    private String userName = "user";
    private String userPassword = "password";

    private String userNameSysProp = "clc._username";
    private String userPasswordSysProp = "clc._password";

    @BeforeClass
    void setup() {
        System.setProperty(userNameSysProp, userName);
        System.setProperty(userPasswordSysProp, userPassword);
    }

    @AfterClass
    void restore() {
        System.clearProperty(userNameSysProp);
        System.clearProperty(userPasswordSysProp);
    }

    @Test
    public void testDefaultConstructor() {
        CredentialsProvider provider = new SystemVariableCredentialsProvider(userNameSysProp, userPasswordSysProp);
        assert provider.getCredentials().isEqualTo(new Credentials(userName, userPassword));
    }

    @Test
    public void testConstructorWithParams() {
        CredentialsProvider provider = new SystemVariableCredentialsProvider(userNameSysProp, userPasswordSysProp);
        Credentials credentials = provider.getCredentials();

        assert userName.equals(credentials.getUsername());
        assert userPassword.equals(credentials.getPassword());
    }
}

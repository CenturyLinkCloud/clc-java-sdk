package com.centurylink.cloud.sdk.core.auth.services.domain.credentials;

import org.mockito.Mockito;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Aliaksandr Krasitski
 */
public class EnvironmentVariableCredentialsProviderTest {

    private String userName = "user";
    private String userPassword = "passwd";

    private String userNameEnvProp = "CLC_USERNAME";
    private String userPasswordEnvProp = "CLC_PASSWD";

    CredentialsProvider mockProvider(CredentialsProvider provider) {
        CredentialsProvider spy = Mockito.spy(provider);
        Mockito.when(spy.getCredentials()).thenReturn(new Credentials(userName, userPassword));

        return spy;
    }

    @Test
    public void testDefaultConstructor() {
        CredentialsProvider provider = mockProvider(new EnvironmentVariableCredentialsProvider());
        assert provider.getCredentials().isEqualTo(new Credentials(userName, userPassword));
    }

    @Test
    public void testConstructorWithParams() {
        CredentialsProvider provider =
            mockProvider(new EnvironmentVariableCredentialsProvider(userNameEnvProp, userPasswordEnvProp));
        Credentials credentials = provider.getCredentials();

        assert userName.equals(credentials.getUsername());
        assert userPassword.equals(credentials.getPassword());
    }
}

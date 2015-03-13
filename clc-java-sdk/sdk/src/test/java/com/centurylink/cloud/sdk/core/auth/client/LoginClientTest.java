package com.centurylink.cloud.sdk.core.auth.client;

import com.centurylink.cloud.sdk.core.auth.client.domain.LoginRequest;
import com.centurylink.cloud.sdk.core.auth.client.domain.LoginResponse;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.Credentials;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.PropertiesFileCredentialsProvider;
import org.testng.annotations.Test;

import javax.ws.rs.client.ResponseProcessingException;

public class LoginClientTest {

    LoginClient client = new LoginClient();

    @Test
    public void testLoginWithCorrectCredentials() {
        Credentials credentials = new PropertiesFileCredentialsProvider().getCredentials();

        LoginResponse response = client.login(
            new LoginRequest(credentials.getUsername(), credentials.getPassword())
        );

        assert response.getAccountAlias() != null;
        assert response.getBearerToken() != null;
    }

    @Test(expectedExceptions = ResponseProcessingException.class)
    public void testLoginWithIncorrectCredentials() {
        client.login(new LoginRequest("incorrect", "account"));
    }

}
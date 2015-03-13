package com.centurylink.cloud.sdk.core.auth.client;

import com.centurylink.cloud.sdk.core.auth.client.domain.LoginRequest;
import com.centurylink.cloud.sdk.core.auth.client.domain.LoginResponse;
import org.testng.annotations.Test;

import javax.ws.rs.client.ResponseProcessingException;

public class LoginClientTest {

    LoginClient client = new LoginClient();

    @Test
    public void testLoginWithCorrectCredentials() {
        LoginResponse response = client.login(new LoginRequest("idrabenia", "RenVortEr9"));

        assert response.getAccountAlias() != null;
        assert response.getBearerToken() != null;
    }

    @Test(expectedExceptions = ResponseProcessingException.class)
    public void testLoginWithIncorrectCredentials() {
        client.login(new LoginRequest("incorrect", "account"));
    }

}
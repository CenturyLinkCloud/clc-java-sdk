package com.centurylink.cloud.sdk.base.auth.services;

import com.centurylink.cloud.sdk.base.auth.client.LoginClient;
import com.centurylink.cloud.sdk.base.auth.client.domain.LoginResponse;
import com.centurylink.cloud.sdk.base.auth.services.domain.credentials.PropertiesFileCredentialsProvider;
import org.testng.annotations.Test;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.core.MultivaluedHashMap;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BearerAuthenticationTest {

    BearerAuthentication auth = new BearerAuthentication(
        new PropertiesFileCredentialsProvider(),
        mock(LoginClient.class)
    );

    private ClientRequestContext stubRequestContext() {
        ClientRequestContext context = mock(ClientRequestContext.class);

        when(context.getHeaders()).thenReturn(new MultivaluedHashMap<>());

        return context;
    }

    @Test
    public void testBearerTokenFilter() throws Exception {
        mockLogin();
        ClientRequestContext context = stubRequestContext();

        auth.filter(context);

        assert context.getHeaders().get("Authorization") != null;
    }

    private void mockLogin() {
        when(auth.getLoginClient().login(any())).thenReturn(
            new LoginResponse("idrabenia", "ALTR", "VA1", null, "BeArERtOkeN")
        );
    }

}
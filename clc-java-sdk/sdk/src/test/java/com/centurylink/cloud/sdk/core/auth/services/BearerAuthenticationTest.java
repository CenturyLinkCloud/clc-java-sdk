package com.centurylink.cloud.sdk.core.auth.services;

import com.centurylink.cloud.sdk.core.auth.client.LoginClient;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.PropertiesFileCredentialsProvider;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.StaticCredentialsProvider;
import org.testng.annotations.Test;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.core.MultivaluedHashMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BearerAuthenticationTest {

    BearerAuthentication auth = new BearerAuthentication(
        new PropertiesFileCredentialsProvider(),
        new LoginClient()
    );

    private ClientRequestContext stubRequestContext() {
        ClientRequestContext context = mock(ClientRequestContext.class);

        when(context.getHeaders()).thenReturn(new MultivaluedHashMap<>());

        return context;
    }

    @Test
    public void testBearerTokenFilter() throws Exception {
        ClientRequestContext context = stubRequestContext();

        auth.filter(context);

        assert context.getHeaders().get("Authorization") != null;
    }

}
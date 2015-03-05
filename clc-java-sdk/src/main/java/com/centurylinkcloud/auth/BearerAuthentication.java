package com.centurylinkcloud.auth;

import com.centurylinkcloud.auth.model.Credentials;
import com.centurylinkcloud.auth.model.LoginResponse;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;

/**
 * @author ilya.drabenia
 */
public class BearerAuthentication implements ClientRequestFilter {
    private final Credentials credentials;
    private static volatile AuthToken token;
    private static volatile String accountAlias;

    public BearerAuthentication(String username, String password) {
        credentials = new Credentials(username, password);

        if (token == null || token.isExpired()) {
            token = requestNewToken();
        }
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        if (token == null || token.isExpired()) {
            token = requestNewToken();
        }

        requestContext
            .getHeaders()
            .putSingle(AUTHORIZATION, token.toHeaderString());
    }

    private AuthToken requestNewToken() {
        LoginResponse result = ClientBuilder.newBuilder()
                .build()
                .target("https://api.tier3.com/v2/authentication/login")
                .request()
                .post(entity(credentials, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(LoginResponse.class);

        accountAlias = result.getAccountAlias();
        return new AuthToken(result.getBearerToken());
    }

    public static String getAccountAlias() {
        return accountAlias;
    }
}

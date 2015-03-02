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
    private volatile ClcToken token;

    public BearerAuthentication(String username, String password) {
        credentials = new Credentials(username, password);
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

    private ClcToken requestNewToken() {
//        LoginResponse result = ClientBuilder.newBuilder()
//                .build()
//                .target("https://api.tier3.com/v2/authentication/login")
//                .request()
//                .post(entity(credentials, MediaType.APPLICATION_JSON_TYPE))
//                .readEntity(LoginResponse.class);
//
//        return new ClcToken(result.bearerToken());
        return new ClcToken(
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ1cm46YXBpLXRpZXIzIiwiYXVkIjoidXJuOn" +
            "RpZXIzLXVzZXJzIiwibmJmIjoxNDI1MzIwOTgwLCJleHAiOjE0MjY1MzA1ODAsInVuaXF1ZV9uYW1lIjoiZHJvYmVueWEua" +
            "Wx5YSIsInVybjp0aWVyMzphY2NvdW50LWFsaWFzIjoiQUxUUiIsInVybjp0aWVyMzpsb2NhdGlvbi1hbGlhcyI6IlZBMSJ9.-" +
            "IrEzM-LZLtGBW6DxhrZOP4YcoXlLRcNtaSSLLufrp0"
        );
    }
}

package com.centurylinkcloud.core.auth.client;

import com.centurylinkcloud.core.auth.client.domain.LoginRequest;
import com.centurylinkcloud.core.auth.client.domain.LoginResponse;
import com.centurylinkcloud.core.client.ErrorResponseFilter;

import javax.ws.rs.client.ClientBuilder;

import static com.centurylinkcloud.core.client.ClcApiConstants.CLC_API_URL;
import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

/**
 * @author ilya.drabenia
 */
public class LoginClient {

    public LoginResponse login(LoginRequest credentials) {
        return
            ClientBuilder
                .newBuilder()
                    .register(new ErrorResponseFilter())
                .build()
                .target(CLC_API_URL + "/authentication/login")
                .request().post(
                    entity(credentials, APPLICATION_JSON_TYPE)
                )
                .readEntity(LoginResponse.class);
    }

}

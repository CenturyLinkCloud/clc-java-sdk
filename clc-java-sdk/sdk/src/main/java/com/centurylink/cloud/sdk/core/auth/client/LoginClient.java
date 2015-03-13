package com.centurylink.cloud.sdk.core.auth.client;

import com.centurylink.cloud.sdk.core.auth.client.domain.LoginRequest;
import com.centurylink.cloud.sdk.core.auth.client.domain.LoginResponse;
import com.centurylink.cloud.sdk.core.client.errors.ErrorProcessingFilter;

import javax.ws.rs.client.ClientBuilder;

import static com.centurylink.cloud.sdk.core.client.ClcApiConstants.CLC_API_URL;
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
                    .register(new ErrorProcessingFilter())
                .build()
                .target(CLC_API_URL + "/authentication/login")
                .request().post(
                    entity(credentials, APPLICATION_JSON_TYPE)
                )
                .readEntity(LoginResponse.class);
    }

}

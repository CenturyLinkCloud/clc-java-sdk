package com.centurylink.cloud.sdk.core.auth.client;

import com.centurylink.cloud.sdk.core.auth.client.domain.LoginRequest;
import com.centurylink.cloud.sdk.core.auth.client.domain.LoginResponse;
import com.centurylink.cloud.sdk.core.client.SdkHttpClient;
import com.centurylink.cloud.sdk.core.config.SdkConfiguration;
import com.google.inject.Inject;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

/**
 * @author ilya.drabenia
 */
public class LoginClient extends SdkHttpClient {

    @Inject
    public LoginClient(SdkConfiguration config) {
        super(config);
    }

    public LoginResponse login(LoginRequest credentials) {
        return
            buildClient()
                .target(CLC_API_URL + "/authentication/login")
                .request().post(
                    entity(credentials, APPLICATION_JSON_TYPE)
                )
                .readEntity(LoginResponse.class);
    }

}

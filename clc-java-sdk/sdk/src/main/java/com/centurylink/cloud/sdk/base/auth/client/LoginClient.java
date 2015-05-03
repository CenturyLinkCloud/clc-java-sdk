package com.centurylink.cloud.sdk.base.auth.client;

import com.centurylink.cloud.sdk.base.client.ClcApiConstants;
import com.centurylink.cloud.sdk.base.auth.client.domain.LoginRequest;
import com.centurylink.cloud.sdk.base.auth.client.domain.LoginResponse;

import static com.centurylink.cloud.sdk.base.client.ClcApiConstants.CLC_API_URL;
import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

/**
 * @author ilya.drabenia
 */
public class LoginClient {

    public LoginResponse login(LoginRequest credentials) {
        return
            ClcApiConstants
                .CLIENT_BUILDER
                .build()
                .target(CLC_API_URL + "/authentication/login")
                .request().post(
                    entity(credentials, APPLICATION_JSON_TYPE)
                )
                .readEntity(LoginResponse.class);
    }

}

package com.centurylink.cloud.sdk.core.client;

import com.centurylink.cloud.sdk.core.auth.services.BearerAuthentication;
import com.centurylink.cloud.sdk.core.client.errors.ErrorProcessingFilter;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import static com.centurylink.cloud.sdk.core.client.ClcApiConstants.CLC_API_URL;

/**
 * @author ilya.drabenia
 */
public class BaseSdkClient {
    protected final BearerAuthentication authentication;

    public BaseSdkClient(BearerAuthentication authFilter) {
        this.authentication = authFilter;
    }

    protected WebTarget client(String target) {
        return
            ClcApiConstants
                .CLIENT_BUILDER
                    .build()
                    .register(authentication)
                    .target(CLC_API_URL + target)
                    .resolveTemplate("accountAlias", authentication.getAccountAlias());
    }

}

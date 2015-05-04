package com.centurylink.cloud.sdk.core.client;

import com.centurylink.cloud.sdk.core.auth.services.BearerAuthentication;
import com.centurylink.cloud.sdk.core.client.errors.ErrorProcessingFilter;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 * @author ilya.drabenia
 */
public class SdkHttpClient {
    public static final String CLC_API_URL = "https://api.tier3.com/v2";

    public static final SdkClientBuilder CLIENT_BUILDER =
        (SdkClientBuilder) ClientBuilder
            .newBuilder()
            .register(new ErrorProcessingFilter());


    protected final BearerAuthentication authentication;

    public SdkHttpClient(BearerAuthentication authFilter) {
        this.authentication = authFilter;
    }

    protected WebTarget client(String target) {
        return
            CLIENT_BUILDER
                .build()
                .register(authentication)
                .target(CLC_API_URL + target)
                .resolveTemplate("accountAlias", authentication.getAccountAlias());
    }

}

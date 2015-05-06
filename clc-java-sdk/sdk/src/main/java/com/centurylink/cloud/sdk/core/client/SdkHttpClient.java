package com.centurylink.cloud.sdk.core.client;

import com.centurylink.cloud.sdk.core.auth.services.BearerAuthentication;
import com.centurylink.cloud.sdk.core.client.errors.ErrorProcessingFilter;
import com.centurylink.cloud.sdk.core.config.SdkConfiguration;

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

    public SdkHttpClient(BearerAuthentication authFilter, SdkConfiguration config) {
        this.authentication = authFilter;

        CLIENT_BUILDER.maxRetries(config.getMaxRetries());
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

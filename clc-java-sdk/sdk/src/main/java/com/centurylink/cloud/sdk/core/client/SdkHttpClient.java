package com.centurylink.cloud.sdk.core.client;

import com.centurylink.cloud.sdk.core.client.errors.ErrorProcessingFilter;
import com.centurylink.cloud.sdk.core.config.SdkConfiguration;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

/**
 * @author ilya.drabenia
 */
public class SdkHttpClient {
    public static final String CLC_API_URL = "https://api.tier3.com/v2";

    public static final SdkClientBuilder CLIENT_BUILDER =
        (SdkClientBuilder) ResteasyClientBuilder
            .newBuilder()
            .register(new ErrorProcessingFilter());

    public SdkHttpClient(SdkConfiguration config) {
        CLIENT_BUILDER.maxRetries(config.getMaxRetries());
        CLIENT_BUILDER.proxyConfig(
            config.getProxyHost(),
            config.getProxyPort(),
            config.getProxyScheme(),
            config.getProxyUsername(),
            config.getProxyPassword()
        );
    }

    protected ResteasyClient buildClient() {
        return CLIENT_BUILDER.build();
    }
}

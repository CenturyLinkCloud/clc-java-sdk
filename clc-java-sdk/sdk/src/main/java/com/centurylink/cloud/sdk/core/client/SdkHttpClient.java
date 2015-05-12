package com.centurylink.cloud.sdk.core.client;

import com.centurylink.cloud.sdk.core.auth.services.ProxyAuthentication;
import com.centurylink.cloud.sdk.core.client.errors.ErrorProcessingFilter;
import com.centurylink.cloud.sdk.core.config.AuthConfig;
import com.centurylink.cloud.sdk.core.config.ProxyConfig;
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

    protected ProxyAuthentication proxyAuthentication;

    public SdkHttpClient(SdkConfiguration config) {
        CLIENT_BUILDER.maxRetries(config.getMaxRetries());
        ProxyConfig proxyConfig = config.getProxy();
        if (proxyConfig != null) {
            CLIENT_BUILDER.defaultProxy(
                proxyConfig.getHostname(),
                proxyConfig.getPort(),
                proxyConfig.getScheme());


            AuthConfig authConfig = proxyConfig.getAuthConfig();
            if (authConfig != null) {
                this.proxyAuthentication = new ProxyAuthentication(
                    authConfig.getUser(),
                    authConfig.getPassword()
                );

                CLIENT_BUILDER.proxyCredentials(
                    authConfig.getUser(),
                    authConfig.getPassword()
                );
            }

        }

    }

    protected ResteasyClient buildClient() {
        ResteasyClient client = CLIENT_BUILDER
            .build();

        if (proxyAuthentication != null) {
            client.register(proxyAuthentication);
        }
        return client;
    }



}

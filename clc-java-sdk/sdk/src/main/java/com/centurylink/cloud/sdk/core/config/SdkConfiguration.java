package com.centurylink.cloud.sdk.core.config;

import com.google.inject.AbstractModule;

/**
 * @author Ilya Drabenia
 */
public class SdkConfiguration {
    private final Integer maxRetries;
    private final ProxyConfig proxy;

    SdkConfiguration() {
        this(new SdkConfigurationBuilder());
    }

    SdkConfiguration(SdkConfigurationBuilder builder) {
        maxRetries = builder.getMaxRetries();
        proxy = builder.getProxyConfig();
    }

    public Integer getMaxRetries() {
        return maxRetries;
    }

    public ProxyConfig getProxy() {
        return proxy;
    }

    public AbstractModule asModule() {
        return new AbstractModule() {
            @Override
            protected void configure() {
                bind(SdkConfiguration.class).toInstance(SdkConfiguration.this);
            }
        };
    }

    public static SdkConfigurationBuilder builder() {
        return new SdkConfigurationBuilder();
    }

}

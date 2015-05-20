package com.centurylink.cloud.sdk.core.config;

import com.google.inject.AbstractModule;

/**
 * @author Ilya Drabenia
 */
public class SdkConfiguration {
    private final Integer maxRetries;
    private String proxyHost;
    private int proxyPort = -1;
    private String proxyScheme;
    private String proxyUsername;
    private String proxyPassword;

    public static final SdkConfiguration DEFAULT = new SdkConfiguration();

    SdkConfiguration() {
        this(new SdkConfigurationBuilder());
    }

    SdkConfiguration(SdkConfigurationBuilder builder) {
        maxRetries = builder.getMaxRetries();
        proxyHost = builder.getProxyHost();
        proxyPort = builder.getProxyPort();
        proxyScheme = builder.getProxyScheme();
        proxyUsername = builder.getProxyUsername();
        proxyPassword = builder.getProxyPassword();
    }

    public Integer getMaxRetries() {
        return maxRetries;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public String getProxyScheme() {
        return proxyScheme;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public String getProxyPassword() {
        return proxyPassword;
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

package com.centurylink.cloud.sdk.core.config;

/**
 * @author Ilya Drabenia
 */
public class SdkConfigurationBuilder {
    private Integer maxRetries = 7;
    private ProxyConfig proxyConfig;

    public SdkConfigurationBuilder maxRetries(Integer retries) {
        maxRetries = retries;
        return this;
    }

    public Integer getMaxRetries() {
        return maxRetries;
    }

    public SdkConfigurationBuilder proxyConfig(ProxyConfig proxyConfig) {
        this.proxyConfig = proxyConfig;
        return this;
    }

    public ProxyConfig getProxyConfig() {
        return proxyConfig;
    }

    public SdkConfiguration build() {
        return new SdkConfiguration(this);
    }




}

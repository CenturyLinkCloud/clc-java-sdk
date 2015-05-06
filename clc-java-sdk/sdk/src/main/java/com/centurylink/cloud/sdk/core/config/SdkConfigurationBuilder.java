package com.centurylink.cloud.sdk.core.config;

/**
 * @author Ilya Drabenia
 */
public class SdkConfigurationBuilder {
    private Integer maxRetries = 3;

    public SdkConfigurationBuilder maxRetries(Integer retries) {
        maxRetries = retries;
        return this;
    }

    public Integer getMaxRetries() {
        return maxRetries;
    }

    public SdkConfiguration build() {
        return new SdkConfiguration(this);
    }
}

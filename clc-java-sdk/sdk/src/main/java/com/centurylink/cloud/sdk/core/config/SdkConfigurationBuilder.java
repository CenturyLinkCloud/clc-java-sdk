package com.centurylink.cloud.sdk.core.config;

/**
 * @author Ilya Drabenia
 */
public class SdkConfigurationBuilder {
    private Integer maxRetries = 7;
    private String proxyHost;
    private int proxyPort = -1;
    private String proxyScheme;
    private String proxyUsername;
    private String proxyPassword;

    public Integer getMaxRetries() {
        return maxRetries;
    }

    public SdkConfigurationBuilder maxRetries(Integer retries) {
        maxRetries = retries;
        return this;
    }

    /**
     * Returns the proxy host name.
     * @return host name
     */
    public String getProxyHost() {
        return proxyHost;
    }

    /**
     * Specify proxy host name (IP or DNS name).
     * @param proxyHost new host name
     * @return current builder instance
     */
    public SdkConfigurationBuilder proxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
        return this;
    }

    /**
     * Returns the proxy port number.
     * @return the proxy port number
     */
    public int getProxyPort() {
        return proxyPort;
    }

    /**
     * Specify proxy port number.
     * @param proxyPort new proxy port value
     * @return current builder instance
     */
    public SdkConfigurationBuilder proxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
        return this;
    }

    /**
     * Returns the name of proxy scheme.
     * @return name of the proxy scheme
     */
    public String getProxyScheme() {
        return proxyScheme;
    }

    /**
     * Specify proxy scheme.
     * @param proxyScheme new name of the proxy scheme
     * @return current builder instance
     */
    public SdkConfigurationBuilder proxyScheme(String proxyScheme) {
        this.proxyScheme = proxyScheme;
        return this;
    }

    /**
     * Returns the proxy user name.
     * @return proxy user name
     */
    public String getProxyUsername() {
        return proxyUsername;
    }

    /**
     * Specify proxy user name.
     * @param proxyUsername new proxy user name
     * @return current builder instance
     */
    public SdkConfigurationBuilder proxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
        return this;
    }

    /**
     * Returns the proxy user password.
     * @return proxy user password
     */
    public String getProxyPassword() {
        return proxyPassword;
    }

    /**
     * Specify proxy scheme.
     * @param proxyPassword new proxy user password
     * @return current builder instance
     */
    public SdkConfigurationBuilder proxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
        return this;
    }

    public SdkConfiguration build() {
        return new SdkConfiguration(this);
    }




}

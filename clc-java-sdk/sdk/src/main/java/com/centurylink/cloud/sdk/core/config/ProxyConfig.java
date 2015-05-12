package com.centurylink.cloud.sdk.core.config;

/**
 * Config for proxy.
 *
 * @author aliaksandr.krasitski
 */
public class ProxyConfig {
    private String hostname;
    private int port = -1;
    private String scheme;
    private AuthConfig authConfig;

    /**
     * Specify host name (IP or DNS name).
     * @param hostname new host name
     * @return current config instance
     */
    public ProxyConfig hostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    /**
     * Returns the host name.
     * @return host name
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * Specify port number.
     * @param port new port value
     * @return current config instance
     */
    public ProxyConfig port(int port) {
        this.port = port;
        return this;
    }

    /**
     * Returns the port number.
     * @return port number
     */
    public int getPort() {
        return port;
    }

    /**
     * Specify scheme.
     * @param scheme new name of the scheme
     * @return current config instance
     */
    public ProxyConfig scheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    /**
     * Returns the name of scheme.
     * @return name of the scheme
     */
    public String getScheme() {
        return scheme;
    }

    /**
     * Specify authentication config.
     * @param authConfig authentication config
     * @return current config instance
     */
    public ProxyConfig authConfig(AuthConfig authConfig) {
        this.authConfig = authConfig;
        return this;
    }

    /**
     * Returns authentication config.
     * @return authentication config
     */
    public AuthConfig getAuthConfig() {
        return authConfig;
    }
}

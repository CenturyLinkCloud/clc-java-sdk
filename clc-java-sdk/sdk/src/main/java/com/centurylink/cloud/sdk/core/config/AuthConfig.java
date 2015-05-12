package com.centurylink.cloud.sdk.core.config;

/**
 * Provided user and password for proxy authentication.
 *
 * @author aliaksandr.krasitski
 */
public class AuthConfig {
    private final String user;
    private final String password;

    public AuthConfig(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}

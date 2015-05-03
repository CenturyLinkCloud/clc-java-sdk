package com.centurylink.cloud.sdk.core.auth.client.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author ilya.drabenia
 */
public class LoginRequest {
    private final String username;
    private final String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @JsonProperty("username")
    public String username() {
        return username;
    }

    @JsonProperty("password")
    public String password() {
        return password;
    }
}

package com.centurylinkcloud.common.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author ilya.drabenia
 */
public class Credentials {
    private final String username;
    private final String password;

    public Credentials(String username, String password) {
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

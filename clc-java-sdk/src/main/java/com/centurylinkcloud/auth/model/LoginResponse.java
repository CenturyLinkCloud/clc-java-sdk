package com.centurylinkcloud.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author ilya.drabenia
 */
public class LoginResponse {
    private final String userName;
    private final String accountAlias;
    private final String locationAlias;
    private final List<String> roles;
    private final String bearerToken;

    public LoginResponse(
            @JsonProperty("userName") String userName,
            @JsonProperty("accountAlias") String accountAlias,
            @JsonProperty("locationAlias") String locationAlias,
            @JsonProperty("roles") List<String> roles,
            @JsonProperty("bearerToken") String bearerToken
    ) {
        this.userName = userName;
        this.accountAlias = accountAlias;
        this.locationAlias = locationAlias;
        this.roles = roles;
        this.bearerToken = bearerToken;
    }

    public String username() {
        return userName;
    }

    public String accountAlias() {
        return accountAlias;
    }

    public String locationAlias() {
        return locationAlias;
    }

    public List<String> roles() {
        return roles;
    }

    public String bearerToken() {
        return bearerToken;
    }
}

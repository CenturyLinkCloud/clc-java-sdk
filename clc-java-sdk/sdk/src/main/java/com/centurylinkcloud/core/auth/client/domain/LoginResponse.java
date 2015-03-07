package com.centurylinkcloud.core.auth.client.domain;

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

    public String getUsername() {
        return userName;
    }

    public String getAccountAlias() {
        return accountAlias;
    }

    public String getLocationAlias() {
        return locationAlias;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getBearerToken() {
        return bearerToken;
    }
}

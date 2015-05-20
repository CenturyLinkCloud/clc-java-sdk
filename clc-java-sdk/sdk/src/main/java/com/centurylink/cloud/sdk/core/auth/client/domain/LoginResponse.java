/*
 * (c) 2015 CenturyLink Cloud. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.centurylink.cloud.sdk.core.auth.client.domain;

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

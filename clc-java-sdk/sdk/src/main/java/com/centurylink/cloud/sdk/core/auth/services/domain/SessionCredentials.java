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

package com.centurylink.cloud.sdk.core.auth.services.domain;

import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.Credentials;

/**
 * @author ilya.drabenia
 */
public class SessionCredentials {
    private final AuthToken token;
    private final String accountAlias;
    private final Credentials credentials;

    public SessionCredentials(AuthToken token, String accountAlias, Credentials credentials) {
        this.token = token;
        this.accountAlias = accountAlias;
        this.credentials = credentials;
    }

    public AuthToken getToken() {
        return token;
    }

    public String getAccountAlias() {
        return accountAlias;
    }

    public Credentials getCredentials() {
        return credentials;
    }
}

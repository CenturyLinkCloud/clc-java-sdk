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

package com.centurylink.cloud.sdk.core.auth.services.domain.credentials;

/**
 * @author ilya.drabenia
 */
public class StaticCredentialsProvider implements CredentialsProvider {
    private final Credentials credentials;

    public StaticCredentialsProvider(String username, String password) {
        this.credentials = new Credentials(username, password);
    }

    public StaticCredentialsProvider(Credentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public Credentials getCredentials() {
        return credentials;
    }

    @Override
    public CredentialsProvider refresh() {
        return this;
    }
}

/*
 * (c) 2015 CenturyLink. All Rights Reserved.
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
 * Class that provide possibilities to receive credentials from Java VM variables.
 * Default property key for username is <b>clc.username</b> and for password is <b>clc.password</b>.
 *
 * <pre>
 *     <code>
 *         java -jar myapp.jar -Dclc.username=Chuck -Dclc.password=Norris
 *     </code>
 * </pre>
 *
 * @author Ilya Drabenia
 */
public class SystemVariableCredentialsProvider implements CredentialsProvider {
    private final String usernameKey;
    private final String passwordKey;

    private volatile Credentials credentials;

    public SystemVariableCredentialsProvider() {
        this("clc.username", "clc.password");
    }

    public SystemVariableCredentialsProvider(String usernameKey, String passwordKey) {
        this.usernameKey = usernameKey;
        this.passwordKey = passwordKey;

        refresh();
    }

    @Override
    public Credentials getCredentials() {
        return credentials;
    }

    @Override
    public CredentialsProvider refresh() {
        credentials = new Credentials(
            System.getProperty(usernameKey),
            System.getProperty(passwordKey)
        );

        return this;
    }

}

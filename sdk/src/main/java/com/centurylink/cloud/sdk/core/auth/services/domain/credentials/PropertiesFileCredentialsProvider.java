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

import com.centurylink.cloud.sdk.core.exceptions.ClcException;

import java.io.IOException;
import java.util.Properties;
import java.util.function.Supplier;

/**
 * Credentials provide that load user credentials from properties file that located at application classpath.
 * Default name of classpath file is <p>centurylink-cloud.properties</p>
 *
 * @author ilya.drabenia
 */
public class PropertiesFileCredentialsProvider implements CredentialsProvider {
    static final String DEFAULT_FILE_NAME = "centurylink-cloud.properties";
    static final String USERNAME_KEY = "clc.username";
    static final String PASSWORD_KEY = "clc.password";

    private final Properties properties = new Properties();
    private final Supplier<Properties> propertiesLoader;

    /**
     * Constructor that load properties from file <p>centurylink-cloud.properties</p>
     */
    public PropertiesFileCredentialsProvider() {
        this(DEFAULT_FILE_NAME);
    }

    /**
     * Constructor that load properties by specified path in classpath
     *
     * @param filePath is properties path in classpath
     */
    public PropertiesFileCredentialsProvider(String filePath) {
        propertiesLoader = propertiesLoader(filePath);

        propertiesLoader.get();
    }

    private Supplier<Properties> propertiesLoader(final String filePath) {
        return () -> loadProperties(filePath);
    }

    private Properties loadProperties(String filePath) {
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream(filePath));
            return properties;
        } catch (IOException e) {
            throw new ClcException(e);
        }
    }

    @Override
    public Credentials getCredentials() {
        return new Credentials(
            properties.getProperty(USERNAME_KEY),
            properties.getProperty(PASSWORD_KEY)
        );
    }

    @Override
    public CredentialsProvider refresh() {
        propertiesLoader.get();
        return this;
    }

}

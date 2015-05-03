package com.centurylink.cloud.sdk.core.auth.services.domain.credentials;

import com.centurylink.cloud.sdk.core.exceptions.ClcException;

import java.io.IOException;
import java.util.Properties;

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
    private final Runnable propertiesLoader;

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

        propertiesLoader.run();
    }

    private Runnable propertiesLoader(final String filePath) {
        return new Runnable() {
            @Override
            public void run() {
                loadProperties(filePath);
            }
        };
    }

    private void loadProperties(String filePath) {
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream(filePath));
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
        propertiesLoader.run();
        return this;
    }

}

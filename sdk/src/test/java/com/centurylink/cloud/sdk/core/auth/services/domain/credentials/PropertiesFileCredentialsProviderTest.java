package com.centurylink.cloud.sdk.core.auth.services.domain.credentials;

import org.testng.annotations.Test;

/**
 * @author Aliaksandr Krasitski
 */
public class PropertiesFileCredentialsProviderTest {

    private String propertiesPath = "props.properties";

//    @Test
//    public void testDefaultConstructor() {
//        CredentialsProvider provider = new PropertiesFileCredentialsProvider();
//        assert provider.getCredentials().isEqualTo(new Credentials("user", "password"));
//    }

    @Test
    public void testConstructorWithParams() {
        new PropertiesFileCredentialsProvider(propertiesPath);
    }
}

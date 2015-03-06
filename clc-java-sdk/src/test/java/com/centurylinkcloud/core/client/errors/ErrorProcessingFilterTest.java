package com.centurylinkcloud.core.client.errors;

import com.centurylinkcloud.core.auth.config.AuthModule;
import com.centurylinkcloud.core.auth.domain.BearerAuthentication;
import com.centurylinkcloud.core.auth.domain.credentials.StaticCredentialsProvider;
import com.google.inject.Guice;
import com.google.inject.ProvisionException;
import org.junit.Test;

/**
 * @author ilya.drabenia
 */
public class ErrorProcessingFilterTest {

    @Test(expected = ClcServiceException.class)
    public void testIncorrectLogin() throws Throwable {
        try {
            Guice
                .createInjector(new AuthModule(new StaticCredentialsProvider("12345", "456789")))
                .getInstance(BearerAuthentication.class);
        } catch (ProvisionException exception) {
            throw
                exception
                    .getCause()
                    .getCause();
        }
    }

}

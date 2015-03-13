package com.centurylink.cloud.sdk.core.client.errors;

import com.centurylink.cloud.sdk.core.auth.config.AuthModule;
import com.centurylink.cloud.sdk.core.auth.domain.BearerAuthentication;
import com.centurylink.cloud.sdk.core.auth.domain.credentials.StaticCredentialsProvider;
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
                .createInjector(
                    new AuthModule(new StaticCredentialsProvider("12345", "456789"))
                )
                .getInstance(BearerAuthentication.class);

        } catch (ProvisionException exception) {
            throw
                exception
                    .getCause()
                    .getCause();
        }
    }

}

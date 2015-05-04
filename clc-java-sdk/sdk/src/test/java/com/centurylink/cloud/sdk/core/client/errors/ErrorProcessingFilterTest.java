package com.centurylink.cloud.sdk.core.client.errors;

import com.centurylink.cloud.sdk.core.auth.AuthModule;
import com.centurylink.cloud.sdk.core.auth.services.BearerAuthentication;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.StaticCredentialsProvider;
import com.centurylink.cloud.sdk.core.client.ClcClientException;
import com.centurylink.cloud.sdk.tests.TestGroups;
import com.google.inject.Guice;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ResponseProcessingException;

/**
 * @author ilya.drabenia
 */
@Test(groups = TestGroups.INTEGRATION)
public class ErrorProcessingFilterTest {

    @Test(expectedExceptions = ClcClientException.class)
    public void testIncorrectLogin() throws Throwable {
        try {
            Guice
                .createInjector(
                    new AuthModule(new StaticCredentialsProvider("12345", "456789"))
                )
                .getInstance(BearerAuthentication.class)
                .filter(Mockito.mock(ClientRequestContext.class));
        } catch (ResponseProcessingException exception) {
            throw exception.getCause();
        }
    }

}

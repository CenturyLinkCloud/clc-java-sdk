package com.centurylink.cloud.sdk.core.client.errors;

import com.centurylink.cloud.sdk.core.TestGroups;
import com.centurylink.cloud.sdk.core.auth.AuthModule;
import com.centurylink.cloud.sdk.core.auth.services.BearerAuthentication;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.StaticCredentialsProvider;
import com.google.inject.Guice;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ResponseProcessingException;

import static org.mockito.Matchers.any;

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

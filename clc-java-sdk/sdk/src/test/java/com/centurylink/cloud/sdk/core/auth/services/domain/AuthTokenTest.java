package com.centurylink.cloud.sdk.core.auth.services.domain;

import org.testng.annotations.Test;

import java.util.Date;
import java.util.GregorianCalendar;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class AuthTokenTest {

    @Test
    public void testNewAuthTokenExpiration() {
        AuthToken token = new AuthToken("token_value");

        assert !token.isExpired();
    }

    @Test
    public void testOutdatedAuthTokenExpiration() {
        AuthToken token = spy(new AuthToken("token_value"));

        final int SPENT_FUTURE_DAYS = 30;
        setCurrentDateInFuture(token, SPENT_FUTURE_DAYS);

        assert token.isExpired();
    }

    private void setCurrentDateInFuture(AuthToken token, final int spentDays) {
        when(token.now())
            .thenReturn(
                new GregorianCalendar() {{
                    setTime(new Date());
                    add(DAY_OF_MONTH, spentDays);
                }}
                .getTime()
            );
    }

}
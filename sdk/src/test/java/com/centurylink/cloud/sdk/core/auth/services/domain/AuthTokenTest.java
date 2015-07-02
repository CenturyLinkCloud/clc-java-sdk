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

    @Test
    public void testToBearerHeader() {
        AuthToken token = new AuthToken("token_value");

        assert "Bearer token_value".equals(token.toHeaderString());
    }

}
/*
 * (c) 2015 CenturyLink Cloud. All Rights Reserved.
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

import java.util.Date;

import static java.util.concurrent.TimeUnit.DAYS;

/**
 * @author ilya.drabenia
 */
public class AuthToken {
    private final String value;
    private final Date receivedAt;

    public AuthToken(String value) {
        this.value = value;
        this.receivedAt = now();
    }

    public String getValue() {
        return value;
    }

    public Date getReceivedAt() {
        return receivedAt;
    }

    public boolean isExpired() {
        return now().getTime() - receivedAt.getTime() > DAYS.toMillis(10);
    }

    public String toHeaderString() {
        return "Bearer " + value;
    }

    Date now() {
        return new Date();
    }
}

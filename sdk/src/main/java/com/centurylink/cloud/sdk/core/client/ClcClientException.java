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

package com.centurylink.cloud.sdk.core.client;

import com.centurylink.cloud.sdk.core.exceptions.ClcException;

/**
 * Base class for all client layer exceptions
 *
 * @author ilya.drabenia
 */
public class ClcClientException extends ClcException {

    public ClcClientException() {
    }

    public ClcClientException(String format, Object... arguments) {
        super(format, arguments);
    }

    public ClcClientException(String message) {
        super(message);
    }

    public ClcClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClcClientException(Throwable cause) {
        super(cause);
    }

}

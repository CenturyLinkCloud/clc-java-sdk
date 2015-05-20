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

package com.centurylink.cloud.sdk.core.services.refs;

import com.centurylink.cloud.sdk.core.services.ClcServiceException;

/**
 * @author ilya.drabenia
 */
public class ReferenceNotResolvedException extends ClcServiceException {

    public ReferenceNotResolvedException() {
    }

    public ReferenceNotResolvedException(String message) {
        super(message);
    }

    public ReferenceNotResolvedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReferenceNotResolvedException(String format, Object... arguments) {
        super(format, arguments);
    }

    public ReferenceNotResolvedException(Throwable cause) {
        super(cause);
    }

}

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

package com.centurylink.cloud.sdk.core.exceptions;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.lang.String.format;

/**
 * Base CenturyLink Cloud SDK Exception
 *
 * @author ilya.drabenia
 */
public class ClcException extends RuntimeException {
    private final List<Exception> subExceptions = new CopyOnWriteArrayList<>();

    public ClcException() {

    }

    public ClcException(String format, Object... arguments) {
        this(format(format, arguments));

        if (lastItem(arguments) instanceof Throwable) {
            this.initCause((Throwable) lastItem(arguments));
        }
    }

    public ClcException(String message) {
        super(message);
    }

    public ClcException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClcException(Throwable cause) {
        super(cause);
    }

    private Object lastItem(Object[] arguments) {
        return arguments[arguments.length - 1];
    }

    public List<Exception> getSubExceptions() {
        return subExceptions;
    }

    public void addSubException(Exception ex) {
        subExceptions.add(ex);
    }
}

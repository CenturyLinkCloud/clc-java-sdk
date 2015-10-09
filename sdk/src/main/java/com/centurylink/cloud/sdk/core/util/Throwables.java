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

package com.centurylink.cloud.sdk.core.util;

import static com.centurylink.cloud.sdk.core.preconditions.Preconditions.checkNotNull;

/**
 * Static utility methods pertaining to instances of {@link Throwable}.
 */
public final class Throwables {
    private Throwables() {}

    /**
     * Propagates {@code throwable} exactly as-is, if and only if it is an
     * instance of {@code declaredType}.
     */
    public static <X extends Throwable> void propagateIfInstanceOf(
        Throwable throwable, Class<X> declaredType) throws X {
        // Check for null is needed to avoid frequent JNI calls to isInstance().
        if (throwable != null && declaredType.isInstance(throwable)) {
            throw declaredType.cast(throwable);
        }
    }

    /**
     * Propagates {@code throwable} exactly as-is, if and only if it is an
     * instance of {@link RuntimeException} or {@link Error}.
     */
    public static void propagateIfPossible(Throwable throwable) {
        propagateIfInstanceOf(throwable, Error.class);
        propagateIfInstanceOf(throwable, RuntimeException.class);
    }

    /**
     * Propagates {@code throwable} as-is if it is an instance of
     * {@link RuntimeException} or {@link Error}, or else as a last resort, wraps
     * it in a {@code RuntimeException} then propagates.
     * <p>
     * This method always throws an exception. The {@code RuntimeException} return
     * type is only for client code to make Java type system happy in case a
     * return value is required by the enclosing method.
     *
     * @param throwable the Throwable to propagate
     * @return nothing will ever be returned; this return type is only for your
     *     convenience, as illustrated in the example above
     */
    public static RuntimeException propagate(Throwable throwable) {
        propagateIfPossible(checkNotNull(throwable));
        throw new RuntimeException(throwable);
    }

}


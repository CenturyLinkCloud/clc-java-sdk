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

package com.centurylink.cloud.sdk.core.preconditions;

import com.google.common.base.Preconditions;

import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Objects.isNull;

/**
 * @author Ilya Drabenia
 */
public abstract class ArgumentPreconditions {

    private static NullPointerException nullPointerException(String message, Object... args) {
        return new NullPointerException(format(message, args));
    }

    private static NullPointerException nullPointerException() {
        return new NullPointerException();
    }

    public static <T> T notNull(T value) {
        return Preconditions.checkNotNull(value);
    }

    public static <T> T notNull(T value, String message) {
        return Preconditions.checkNotNull(value, message);
    }

    public static <T> T[] allItemsNotNull(T[] items, String containerName) {
        allItemsNotNull(asList(items), containerName);

        return items;
    }

    public static <T> List<T> allItemsNotNull(List<T> items, String containerName) {
        if (items == null) {
            throw nullPointerException("%s must be not null", containerName);
        }

        for (int i = 0; i < items.size(); i += 1) {
            if (isNull(items.get(i))) {
                throw nullPointerException("%s at position %s is null", containerName, i);
            }
        }

        return items;
    }
}

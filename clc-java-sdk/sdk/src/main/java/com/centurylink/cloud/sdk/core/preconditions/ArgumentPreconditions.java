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

    public static <T> T[] allItemsNotNull(T[] items) {
        allItemsNotNull(asList(items));
        return items;
    }

    public static <T> List<T> allItemsNotNull(List<T> items) {
        if (items == null) {
            throw nullPointerException();
        }

        for (int i = 0; i < items.size(); i += 1) {
            if (isNull(items.get(i))) {
                throw nullPointerException("Element at position %s is null", i);
            }
        }

        return items;
    }
}

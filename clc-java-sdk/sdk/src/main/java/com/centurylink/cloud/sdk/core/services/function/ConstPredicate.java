package com.centurylink.cloud.sdk.core.services.function;

import java.util.function.Predicate;

/**
 * @author Ilya Drabenia
 */
public class ConstPredicate<T> implements Predicate<T> {
    private final boolean defaultValue;

    public ConstPredicate(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public boolean test(T t) {
        return defaultValue;
    }

    public boolean getDefaultValue() {
        return defaultValue;
    }

    public static <T> ConstPredicate<T> cast(Predicate<T> predicate) {
        return (ConstPredicate<T>) predicate;
    }
}

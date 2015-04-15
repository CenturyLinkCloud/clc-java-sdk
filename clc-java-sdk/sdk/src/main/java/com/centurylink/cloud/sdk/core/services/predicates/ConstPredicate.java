package com.centurylink.cloud.sdk.core.services.predicates;

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

}

package com.centurylink.cloud.sdk.core.services.predicates;

import java.util.function.Predicate;

/**
 * @author Ilya Drabenia
 */
public abstract class Predicates {

    public static <T> Predicate<T> notNull() {
        return (T item) -> item != null;
    }

    public static <T> Predicate<T> alwaysTrue() {
        return new ConstPredicate<>(true);
    }

    public static <T> Predicate<T> alwaysFalse() {
        return new ConstPredicate<>(false);
    }

}

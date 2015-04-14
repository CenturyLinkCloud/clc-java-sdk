package com.centurylink.cloud.sdk.core.services;

import java.util.function.Predicate;

/**
 * @author Ilya Drabenia
 */
public abstract class Predicates {

    public static <T> Predicate<T> notNull() {
        return (T item) -> item != null;
    }

}

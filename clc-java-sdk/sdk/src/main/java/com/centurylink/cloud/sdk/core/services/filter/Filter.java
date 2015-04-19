package com.centurylink.cloud.sdk.core.services.filter;

/**
 * @author Ilya Drabenia
 */
public interface Filter<T extends Filter<T>> {

    T and(T otherFilter);

    T or(T otherFilter);

    @SafeVarargs
    static <T extends Filter<T>> T and(T... filters) {
        return Filters.reduce(
            (obj, otherObj) -> obj.and(otherObj),
            filters
        );
    }

    @SafeVarargs
    static <T extends Filter<T>> T or(T... filters) {
        return Filters.reduce(
            (obj, otherObj) -> obj.or(otherObj),
            filters
        );
    }

}

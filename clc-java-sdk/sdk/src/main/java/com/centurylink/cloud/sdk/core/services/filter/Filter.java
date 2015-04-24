package com.centurylink.cloud.sdk.core.services.filter;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * @author Ilya Drabenia
 */
public interface Filter<T extends Filter<T>> {

    T and(T otherFilter);

    T or(T otherFilter);

    @SafeVarargs
    static <T extends Filter<T>> T and(T... filters) {
        return and(asList(filters));
    }

    @SafeVarargs
    static <T extends Filter<T>> T or(T... filters) {
        return and(asList(filters));
    }

    static <T extends Filter<T>> T and(List<T> filters) {
        return Filters.reduce(
            filters,
            (obj, otherObj) -> obj.and(otherObj)
        );
    }

    static <T extends Filter<T>> T or(List<T> filters) {
        return Filters.reduce(
            filters,
            (obj, otherObj) -> obj.or(otherObj)
        );
    }

}

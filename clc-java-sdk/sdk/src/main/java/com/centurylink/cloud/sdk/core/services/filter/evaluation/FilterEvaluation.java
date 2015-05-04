package com.centurylink.cloud.sdk.core.services.filter.evaluation;

import com.centurylink.cloud.sdk.core.services.filter.Filter;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Ilya Drabenia
 */
public interface FilterEvaluation<T extends Filter<T>> {

    @SuppressWarnings("unchecked")
    default <K> Stream<K> applyFindLazy(Function<T, Stream<K>> func) {
        return func.apply((T) this);
    }

}

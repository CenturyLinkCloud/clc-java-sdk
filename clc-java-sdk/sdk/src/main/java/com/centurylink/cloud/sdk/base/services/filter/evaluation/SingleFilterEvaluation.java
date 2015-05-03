package com.centurylink.cloud.sdk.base.services.filter.evaluation;

import com.centurylink.cloud.sdk.base.services.filter.Filter;

import java.util.function.Function;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Ilya Drabenia
 */
public class SingleFilterEvaluation<T extends Filter<T>> implements FilterEvaluation<T> {
    private final T filter;

    public SingleFilterEvaluation(T filter) {
        this.filter = checkNotNull(filter, "Filter must be not a null");
    }

    @Override
    public <K> Stream<K> applyFindLazy(Function<T, Stream<K>> func) {
        return func.apply(filter);
    }
}

package com.centurylink.cloud.sdk.core.services.filter.evaluation;

import com.centurylink.cloud.sdk.core.services.filter.Filter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Ilya Drabenia
 */
@SuppressWarnings("unchecked")
public class AndEvaluation<T extends Filter<T>> extends LogicalOperatorEvaluation<T> {

    public <K, V> AndEvaluation(FilterEvaluation<T> evaluation, T filter,
                                Function<K, V> identityKeyExtractor) {
        super(evaluation, filter, identityKeyExtractor);
    }

    @SuppressWarnings("unchecked")
    private <K> Predicate<K> intersectionByKey() {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();

        return t -> seen.putIfAbsent(identityKeyExtractor.apply(t), Boolean.TRUE) != null;
    }

    @Override
    public <K> Stream<K> applyFindLazy(Function<T, Stream<K>> func) {
        return
            Stream
                .concat(
                    evaluation.applyFindLazy(func),
                    filter.applyFindLazy(func)
                )
                .filter(intersectionByKey());
    }
}

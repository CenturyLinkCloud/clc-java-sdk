package com.centurylink.cloud.sdk.core.services.filter.evaluation;

import com.centurylink.cloud.sdk.core.services.filter.Filter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Ilya Drabenia
 */
public class OrEvaluation<T extends Filter<T>> extends LogicalOperatorEvaluation<T> {

    public <K, V> OrEvaluation(FilterEvaluation<T> evaluation, T filter,
                        Function<K, V> keyExtractor) {
        super(evaluation, filter, keyExtractor);
    }

    @SuppressWarnings("unchecked")
    private <K> Predicate<K> distinctByKey() {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();

        return t -> seen.putIfAbsent(identityKeyExtractor.apply(t), Boolean.TRUE) == null;
    }

    @Override
    public <K> Stream<K> applyFindLazy(Function<T, Stream<K>> func) {
        return Stream
            .concat(
                evaluation.applyFindLazy(func),
                func.apply(filter)
            )
            .filter(distinctByKey());
    }

}

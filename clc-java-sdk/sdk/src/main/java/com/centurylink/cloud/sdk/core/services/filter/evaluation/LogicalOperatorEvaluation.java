package com.centurylink.cloud.sdk.core.services.filter.evaluation;

import com.centurylink.cloud.sdk.core.services.filter.Filter;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Ilya Drabenia
 */
public class LogicalOperatorEvaluation<T extends Filter<T>> implements FilterEvaluation<T> {
    protected final FilterEvaluation<T> evaluation;
    protected final T filter;
    protected final Function identityKeyExtractor;

    public LogicalOperatorEvaluation(FilterEvaluation<T> evaluation, T filter,
                                     Function identityKeyExtractor) {
        this.evaluation = checkNotNull(evaluation);
        this.filter = checkNotNull(filter);
        this.identityKeyExtractor = checkNotNull(identityKeyExtractor);
    }
}

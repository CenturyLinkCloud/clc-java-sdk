package com.centurylink.cloud.sdk.core.services.filter;

import com.centurylink.cloud.sdk.core.services.filter.evaluation.FilterEvaluation;
import com.centurylink.cloud.sdk.core.services.filter.evaluation.SingleFilterEvaluation;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Ilya Drabenia
 */
@SuppressWarnings("unchecked")
public abstract class AbstractResourceFilter<T extends AbstractResourceFilter<T>> implements Filter<T> {
    protected FilterEvaluation<T> evaluation = new SingleFilterEvaluation<T>((T) this);

    public AbstractResourceFilter() {
    }

    public FilterEvaluation<T> getEvaluation() {
        return evaluation;
    }

    @Override
    public <K> Stream<K> applyFindLazy(Function<T, Stream<K>> func) {
        return evaluation.applyFindLazy(func);
    }
}

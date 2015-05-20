package com.centurylink.cloud.sdk.core.services.filter;

import com.centurylink.cloud.sdk.core.CastMixin;
import com.centurylink.cloud.sdk.core.services.filter.evaluation.FilterEvaluation;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * @author Ilya Drabenia
 */
public interface Filter<T extends Filter<T>> extends CastMixin, FilterEvaluation<T> {

    /**
     * Method allow to compose two filter in one that filtering results equals to sequencial applying
     * first filtering results to filtering results of second filter
     *
     * @param otherFilter is list of filters
     * @return {@link com.centurylink.cloud.sdk.core.services.filter.Filter}
     * @throws java.lang.NullPointerException if other filter is null
     */
    T and(T otherFilter);

    /**
     * Method allow to compose two filter in one that filtering result equals to composition of
     * filtering results provided by each filter
     *
     * @param otherFilter is list of filters
     * @return {@link com.centurylink.cloud.sdk.core.services.filter.Filter}
     * @throws java.lang.NullPointerException if other filter is null
     */
    T or(T otherFilter);

    /**
     * Method allow to compose multiple filter in one that filtering results equals to sequencial applying
     * previous filtering results to next filter
     *
     * @param filters is list of filters
     * @param <T> type of target filter
     * @return {@link com.centurylink.cloud.sdk.core.services.filter.Filter}
     */
    @SafeVarargs
    static <T extends Filter<T>> T and(T... filters) {
        return and(asList(filters));
    }

    /**
     * Method allow to compose multiple filter in one that filtering result equals to composition of
     * filtering results provided by each filter
     *
     * @param filters is list of filters
     * @param <T> type of target filter
     * @return {@link com.centurylink.cloud.sdk.core.services.filter.Filter}
     */
    @SafeVarargs
    static <T extends Filter<T>> T or(T... filters) {
        return or(asList(filters));
    }

    /**
     * Method allow to compose multiple filter in one that returns results equals to sequencial applying
     * previous filtering results to next filter
     *
     * @param filters is list of filters
     * @param <T> type of target filter
     * @return {@link com.centurylink.cloud.sdk.core.services.filter.Filter}
     */
    static <T extends Filter<T>> T and(List<T> filters) {
        return Filters.reduce(
            filters,
            (obj, otherObj) -> obj.and(otherObj)
        );
    }

    /**
     * Method allow to compose multiple filter in one that filtering result equals to composition of
     * filtering results provided by each filter
     *
     * @param filters is list of filters
     * @param <T> type of target filter
     * @return {@link com.centurylink.cloud.sdk.core.services.filter.Filter}
     */
    static <T extends Filter<T>> T or(List<T> filters) {
        return Filters.reduce(
            filters,
            (obj, otherObj) -> obj.or(otherObj)
        );
    }

}

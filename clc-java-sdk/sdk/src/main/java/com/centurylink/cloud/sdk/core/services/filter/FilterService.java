package com.centurylink.cloud.sdk.core.services.filter;

import java.util.List;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * @author Ilya Drabenia
 */
public interface FilterService<F extends Filter<F>, M> {

    /**
     * Method find all resources satisfied by specified filter
     *
     * @param filter is not null search filter
     * @return list of found resource
     */
    default List<M> find(F filter) {
        checkNotNull(filter, "Filter must be not a null");

        return findLazy(filter).collect(toList());
    }

    /**
     * Method find all resources satisfied by specified filter
     *
     * @param filter is not null search filter
     * @return stream of found resource
     */
    Stream<M> findLazy(F filter);

}

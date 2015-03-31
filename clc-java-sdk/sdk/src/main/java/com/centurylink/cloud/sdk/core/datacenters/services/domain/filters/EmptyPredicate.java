package com.centurylink.cloud.sdk.core.datacenters.services.domain.filters;

import java.util.function.Predicate;

/**
 * @author Ilya Drabenia
 */
public class EmptyPredicate<T> implements Predicate<T> {

    @Override
    public boolean test(T t) {
        return true;
    }

}

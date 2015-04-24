package com.centurylink.cloud.sdk.core.services.refs;

import com.centurylink.cloud.sdk.core.services.ResourceNotFoundException;

import java.util.function.Supplier;

/**
 * Class represent reference on existing resource.
 * If represented resource is not exists yet in system in all cases will be thrown exceptions.
 *
 * @author ilya.drabenia
 */
public interface Reference {

    @SuppressWarnings("unchecked")
    default <T extends Reference> T as(Class<T> type) {
        return (T) this;
    }

    default <T extends Reference> boolean is(Class<T> type) {
        return type == getClass();
    }

    static Supplier<ResourceNotFoundException> notFound(Reference reference) {
        return () -> new  ResourceNotFoundException("Reference %s not resolved", reference);
    }

    /**
     * Method convert reference to filter object
     *
     * @return filter object
     */
    Object asFilter();

}

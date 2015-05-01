package com.centurylink.cloud.sdk.core.services.refs;

import com.centurylink.cloud.sdk.core.services.CastMixin;
import com.centurylink.cloud.sdk.core.services.ResourceNotFoundException;

import java.util.function.Supplier;

/**
 * Class represent reference on existing resource.
 * If represented resource is not exists yet in system in all cases will be thrown exceptions.
 *
 * @author ilya.drabenia
 */
public interface Reference extends CastMixin {

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

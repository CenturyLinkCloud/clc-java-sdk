package com.centurylink.cloud.sdk.core.services.refs;

import com.centurylink.cloud.sdk.core.CastMixin;
import com.centurylink.cloud.sdk.core.ToStringMixin;
import com.centurylink.cloud.sdk.core.services.filter.Filter;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Class represent reference on existing resource.
 * If represented resource is not exists yet in system in all cases will be thrown exceptions.
 *
 * @author ilya.drabenia
 */
@SuppressWarnings("unchecked")
public interface Reference extends CastMixin, ToStringMixin {

    static Supplier<ReferenceNotResolvedException> notFound(Reference reference) {
        return () -> new ReferenceNotResolvedException("Reference %s not resolved", reference);
    }

    static <T, K extends Filter<K>> T evalUsingFindByFilter(Reference ref,
                                                            Function<K, List<T>> findOperation) {
        List<T> results = findOperation.apply((K) ref.asFilter());

        if (results.size() == 1) {
            return results.get(0);
        } else if (results.size() > 1) {
            throw new ReferenceNotResolvedException(
                "Resource by reference %s not found", ref.toReadableString()
            );
        } else {
            throw new ReferenceNotResolvedException(
                "Reference %s point to multiple resource", ref.toReadableString()
            );
        }
    }

    /**
     * Method convert reference to filter object
     *
     * @return filter object
     */
    Filter asFilter();

}

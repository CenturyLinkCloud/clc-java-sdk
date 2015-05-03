package com.centurylink.cloud.sdk.core.services;

import com.centurylink.cloud.sdk.core.services.filter.Filter;
import com.centurylink.cloud.sdk.core.services.filter.FilterService;
import com.centurylink.cloud.sdk.core.services.refs.Reference;
import com.centurylink.cloud.sdk.core.services.refs.ReferenceNotResolvedException;

import java.util.List;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * @author Ilya Drabenia
 */
public interface QueryService<R extends Reference<F>, F extends Filter<F>, M>
        extends FilterService<F, M> {

    /**
     * Method allow to resolve resource metadata by reference.
     *
     * @param reference is not null reference to resource
     * @return resolved resource metadata
     * @throws com.centurylink.cloud.sdk.core.services.refs.ReferenceNotResolvedException
     *         If represented resource is not exists yet in system or it will be found multiple resources
     *         satisfied by reference criteria
     */
    default M findByRef(R reference) {
        checkNotNull(reference, "Reference must be not a null");

        List<M> results = this.find(reference.asFilter());

        if (results.size() == 1) {
            return results.get(0);
        } else if (results.size() > 1) {
            throw new ReferenceNotResolvedException(
                "Resource by reference %s not found", reference.toReadableString()
            );
        } else {
            throw new ReferenceNotResolvedException(
                "Reference %s point to multiple resource", reference.toReadableString()
            );
        }
    }

}

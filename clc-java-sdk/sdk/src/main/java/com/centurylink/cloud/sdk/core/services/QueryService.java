/*
 * (c) 2015 CenturyLink Cloud. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.centurylink.cloud.sdk.core.services;

import com.centurylink.cloud.sdk.core.services.filter.Filter;
import com.centurylink.cloud.sdk.core.services.filter.FilterService;
import com.centurylink.cloud.sdk.core.services.refs.Reference;
import com.centurylink.cloud.sdk.core.services.refs.ReferenceNotResolvedException;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

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
                "Reference %s point to multiple resource", reference.toReadableString()
            );
        } else {
            throw new ReferenceNotResolvedException(
                "Resource by reference %s not found", reference.toReadableString()
            );
        }
    }

}

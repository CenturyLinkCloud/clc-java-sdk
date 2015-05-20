/*
 * (c) 2015 CenturyLink. All Rights Reserved.
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

package com.centurylink.cloud.sdk.servers.client.domain.server;

import com.centurylink.cloud.sdk.core.client.ClcClientException;
import com.centurylink.cloud.sdk.core.exceptions.ClcException;
import com.centurylink.cloud.sdk.core.exceptions.ErrorsContainer;
import com.centurylink.cloud.sdk.core.services.ClcServiceException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Boolean.TRUE;
import static java.util.stream.Collectors.toList;

public class BaseServerListResponse extends ArrayList<BaseServerResponse> {

    public BaseServerListResponse(int initialCapacity) {
        super(initialCapacity);
    }

    public BaseServerListResponse() {
    }

    public BaseServerListResponse(Collection<? extends BaseServerResponse> collection) {
        super(collection);
    }

    public List<Exception> listExceptions() {
        return
            this
                .stream()
                .filter(this::isJobNotQueued)
                .map(this::errorMessage)
                .map(ClcClientException::new)
                .collect(toList());
    }

    private boolean isJobNotQueued(BaseServerResponse r) {
        return r.getErrorMessage() != null || !TRUE.equals(r.getQueued());
    }

    private String errorMessage(BaseServerResponse response) {
        if (response.getErrorMessage() != null) {
            return String.format(
                "Job for server %s is not queued with error message \"%s\"",
                response.getServer(),
                response.getErrorMessage()
            );
        } else {
            return String.format("Job for server %s not queued", response.getServer());
        }
    }

    public ClcException summaryException() {
        return
            new ErrorsContainer(ClcServiceException::new)
                .addAll(listExceptions())
                .summaryException();
    }

    public boolean hasErrors() {
        return listExceptions().size() > 0;
    }
}

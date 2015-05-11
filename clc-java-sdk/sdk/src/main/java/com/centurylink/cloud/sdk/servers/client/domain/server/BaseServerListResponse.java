package com.centurylink.cloud.sdk.servers.client.domain.server;

import com.centurylink.cloud.sdk.core.client.ClcClientException;
import com.centurylink.cloud.sdk.core.exceptions.ClcException;
import com.centurylink.cloud.sdk.core.exceptions.ErrorsContainer;
import com.centurylink.cloud.sdk.core.services.ClcServiceException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            Stream
                .concat(
                    this
                        .stream()
                        .filter(r -> r.getErrorMessage() != null)
                        .map(r -> new ClcClientException(r.getErrorMessage())),

                    this
                        .stream()
                        .filter(r -> !r.getQueued())
                        .map(r -> new ClcClientException("Job for resource %s not quequed", r.getServer()))
                )
                .collect(toList());
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

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
        return r.getErrorMessage() != null || !r.getQueued();
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

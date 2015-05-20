package com.centurylink.cloud.sdk.core.client.retry;

import org.apache.http.HttpResponse;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.protocol.HttpContext;

import java.util.List;

import static java.util.Arrays.asList;
import static org.jboss.resteasy.util.HttpResponseCodes.*;

/**
 * @author Ilya Drabenia
 */
public class ClcRetryStrategy implements ServiceUnavailableRetryStrategy {

    /**
     * Maximum number of allowed retries if the server responds with a HTTP code
     * in our retry code list. Default value is 1.
     */
    private final int maxRetries;

    /**
     * Retry interval between subsequent requests, in milliseconds. Default
     * value is 1 second.
     */
    private final long retryInterval;

    public ClcRetryStrategy(int maxRetries, int retryInterval) {
        super();
        if (maxRetries < 1) {
            throw new IllegalArgumentException("MaxRetries must be greater than 1");
        }
        if (retryInterval < 1) {
            throw new IllegalArgumentException("Retry interval must be greater than 1");
        }
        this.maxRetries = maxRetries;
        this.retryInterval = retryInterval;
    }

    public ClcRetryStrategy() {
        this(1, 1000);
    }

    @Override
    public boolean retryRequest(final HttpResponse response, int executionCount, final HttpContext context) {
        return executionCount <= maxRetries && isRequestFailed(response);
    }

    private boolean isRequestFailed(HttpResponse response) {
        List<Integer> successResultCodes = asList(SC_OK, SC_CREATED, SC_ACCEPTED, SC_NO_CONTENT, SC_NOT_FOUND);
        return !successResultCodes.contains(response.getStatusLine().getStatusCode());
    }

    @Override
    public long getRetryInterval() {
        return retryInterval;
    }
}

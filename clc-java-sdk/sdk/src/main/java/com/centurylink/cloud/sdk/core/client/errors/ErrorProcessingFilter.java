package com.centurylink.cloud.sdk.core.client.errors;

import com.centurylink.cloud.sdk.core.client.ClcClientException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import java.io.IOException;

import static java.util.Arrays.asList;
import static javax.ws.rs.core.Response.Status.*;

/**
 * @author ilya.drabenia
 */
public class ErrorProcessingFilter implements ClientResponseFilter {

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        if (BAD_REQUEST.getStatusCode() == responseContext.getStatusInfo().getStatusCode()) {
            String response = IOUtils.toString(responseContext.getEntityStream(), "UTF-8");

            throw new ClcClientException(response);
        }
    }

    /**
     * Returns true if response contains status codes:
     * {@code OK}
     * {@code CREATED}
     * {@code ACCEPTED}
     * {@code NO_CONTENT}
     * {@code NOT_FOUND} - added because client returns only 404 status code and response doesn't contain body,
     * in case when requested resource not found
     *
     * @param responseContext
     * @return true if response contains acceptable status codes
     */
    private boolean isResponseSuccess(ClientResponseContext responseContext) {
        return asList(OK, CREATED, ACCEPTED, NO_CONTENT, NOT_FOUND).contains(responseContext.getStatusInfo());
    }

    private ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}

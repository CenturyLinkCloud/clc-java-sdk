package com.centurylink.cloud.sdk.core.client.errors;

import com.centurylink.cloud.sdk.core.client.ClcClientException;
import com.centurylink.cloud.sdk.core.exceptions.ClcException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import java.io.IOException;
import java.io.InputStream;

import static java.util.Arrays.asList;
import static javax.ws.rs.core.Response.Status.*;

/**
 * @author ilya.drabenia
 */
public class ErrorProcessingFilter implements ClientResponseFilter {

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        if (!isResponseSuccess(responseContext)) {
            InputStream entityStream = responseContext.getEntityStream();
            ErrorMessageResponse response = objectMapper().readValue(entityStream, ErrorMessageResponse.class);

            if (response.getMessage() != null) {
                throw new ClcClientException(response.getMessage(), new ClcException(objectMapper().writeValueAsString(response.getModelState())));
            }
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

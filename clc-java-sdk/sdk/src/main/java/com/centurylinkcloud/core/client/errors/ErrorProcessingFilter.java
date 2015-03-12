package com.centurylinkcloud.core.client.errors;

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
            Response response = objectMapper().readValue(entityStream, Response.class);

            if (response.getMessage() != null) {
                throw new ClcServiceException(response.getMessage());
            }
        }
    }

    private boolean isResponseSuccess(ClientResponseContext responseContext) {
        return asList(OK, CREATED, ACCEPTED).contains(responseContext.getStatusInfo());
    }

    private ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}

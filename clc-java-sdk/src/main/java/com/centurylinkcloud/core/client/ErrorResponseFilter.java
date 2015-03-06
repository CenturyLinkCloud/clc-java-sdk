package com.centurylinkcloud.core.client;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import java.io.IOException;
import java.io.InputStream;

import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.OK;

/**
 * @author ilya.drabenia
 */
public class ErrorResponseFilter implements ClientResponseFilter {

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        if (responseContext.getStatusInfo() != OK && responseContext.getStatusInfo() != CREATED) {
            InputStream entityStream = responseContext.getEntityStream();
            Response response = objectMapper().readValue(entityStream, Response.class);

            if (response.getMessage() != null) {
                throw new IllegalStateException(response.getMessage());
            }
        }
    }

    private ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}

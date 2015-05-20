package com.centurylink.cloud.sdk.core.client.errors;

import org.apache.commons.io.IOUtils;

import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * @author Ilya Drabenia
 */
public class ClcHttpClientException extends ResponseProcessingException {

    public ClcHttpClientException(ClientResponseContext response) throws IOException {
        super(
            Response.status(response.getStatus()).build(),
            statusInfo(response) + "\n" +
            responseBody(response)
        );
    }

    private static String responseBody(ClientResponseContext response) throws IOException {
        return IOUtils.toString(response.getEntityStream(), "UTF-8");
    }

    private static String statusInfo(ClientResponseContext response) {
        final Response.StatusType statusInfo;

        if (response != null) {
            statusInfo = response.getStatusInfo();
        } else {
            statusInfo = Response.Status.INTERNAL_SERVER_ERROR;
        }

        return "HTTP " + statusInfo.getStatusCode() + ' ' + statusInfo.getReasonPhrase();
    }

}

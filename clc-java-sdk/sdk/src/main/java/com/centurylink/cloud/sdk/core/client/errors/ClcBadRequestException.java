package com.centurylink.cloud.sdk.core.client.errors;

import org.apache.commons.io.IOUtils;

import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * @author Ilya Drabenia
 */
public class ClcBadRequestException extends ClcHttpClientException {

    public ClcBadRequestException(ClientResponseContext response) throws IOException {
        super(response);
    }

}

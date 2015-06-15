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

package com.centurylink.cloud.sdk.core.client.errors;

import com.centurylink.cloud.sdk.core.client.ClcClientException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.RedirectionException;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import java.io.IOException;

import static java.util.Arrays.asList;
import static javax.ws.rs.core.Response.Status.*;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;

/**
 * @author ilya.drabenia
 */
public class ErrorProcessingFilter implements ClientResponseFilter {

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        if (responseContext.getStatus() == SC_BAD_REQUEST) {
            throw new ClcBadRequestException(responseContext);
        } else if (responseContext.getStatus() >= 400) {
            throw new ClcHttpClientException(responseContext);
        }
    }

}

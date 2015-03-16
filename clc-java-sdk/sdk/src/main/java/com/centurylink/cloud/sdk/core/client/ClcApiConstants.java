package com.centurylink.cloud.sdk.core.client;

import com.centurylink.cloud.sdk.core.auth.services.BearerAuthentication;
import com.centurylink.cloud.sdk.core.client.errors.ErrorProcessingFilter;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * @author ilya.drabenia
 */
public interface ClcApiConstants {

    static final String CLC_API_URL = "https://api.tier3.com/v2";

    static final ClientBuilder CLIENT_BUILDER = ClientBuilder
            .newBuilder()
            .register(new ErrorProcessingFilter());

}

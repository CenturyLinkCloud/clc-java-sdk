package com.centurylink.cloud.sdk.core.client;

import com.centurylink.cloud.sdk.core.client.errors.ErrorProcessingFilter;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import javax.ws.rs.client.ClientBuilder;

/**
 * @author ilya.drabenia
 */
public interface ClcApiConstants {

    static final String CLC_API_URL = "https://api.tier3.com/v2";

    static final SdkClientBuilder CLIENT_BUILDER =
        (SdkClientBuilder) ClientBuilder
            .newBuilder()
            .register(new ErrorProcessingFilter());

}

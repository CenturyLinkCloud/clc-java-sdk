package com.centurylink.cloud.sdk.base.client;

import com.centurylink.cloud.sdk.base.client.errors.ErrorProcessingFilter;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import javax.ws.rs.client.ClientBuilder;

/**
 * @author ilya.drabenia
 */
public interface ClcApiConstants {

    static final String CLC_API_URL = "https://api.tier3.com/v2";

    static final ResteasyClientBuilder CLIENT_BUILDER =
        (ResteasyClientBuilder) ClientBuilder
            .newBuilder()
            .register(new ErrorProcessingFilter());

}

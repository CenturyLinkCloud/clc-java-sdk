package com.centurylink.cloud.sdk.networks.client;

import com.centurylink.cloud.sdk.core.auth.services.BearerAuthentication;
import com.centurylink.cloud.sdk.core.client.SdkHttpClient;
import com.centurylink.cloud.sdk.core.config.SdkConfiguration;
import com.google.inject.Inject;

/**
 * @author ilya.drabenia
 */
public class NetworksClient extends SdkHttpClient {

    @Inject
    public NetworksClient(BearerAuthentication authFilter, SdkConfiguration config) {
        super(authFilter, config);
    }

}

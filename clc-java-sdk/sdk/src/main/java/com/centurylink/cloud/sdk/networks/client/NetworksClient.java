package com.centurylink.cloud.sdk.networks.client;

import com.centurylink.cloud.sdk.core.auth.services.BearerAuthentication;
import com.centurylink.cloud.sdk.core.client.AuthenticatedSdkHttpClient;
import com.centurylink.cloud.sdk.core.config.SdkConfiguration;
import com.google.inject.Inject;

/**
 * @author ilya.drabenia
 */
public class NetworksClient extends AuthenticatedSdkHttpClient {

    @Inject
    public NetworksClient(BearerAuthentication authFilter, SdkConfiguration config) {
        super(authFilter, config);
    }

}

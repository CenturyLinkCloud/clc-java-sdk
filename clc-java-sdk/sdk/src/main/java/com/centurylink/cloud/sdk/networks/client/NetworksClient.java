package com.centurylink.cloud.sdk.networks.client;

import com.centurylink.cloud.sdk.core.auth.services.BearerAuthentication;
import com.centurylink.cloud.sdk.core.client.ClcClient;
import com.google.inject.Inject;

/**
 * @author ilya.drabenia
 */
public class NetworksClient extends ClcClient {

    @Inject
    public NetworksClient(BearerAuthentication authFilter) {
        super(authFilter);
    }

}

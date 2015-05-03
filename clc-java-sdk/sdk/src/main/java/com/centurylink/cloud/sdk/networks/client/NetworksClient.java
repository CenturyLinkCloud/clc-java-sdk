package com.centurylink.cloud.sdk.networks.client;

import com.centurylink.cloud.sdk.base.auth.services.BearerAuthentication;
import com.centurylink.cloud.sdk.base.client.BaseSdkClient;
import com.google.inject.Inject;

/**
 * @author ilya.drabenia
 */
public class NetworksClient extends BaseSdkClient {

    @Inject
    public NetworksClient(BearerAuthentication authFilter) {
        super(authFilter);
    }

}

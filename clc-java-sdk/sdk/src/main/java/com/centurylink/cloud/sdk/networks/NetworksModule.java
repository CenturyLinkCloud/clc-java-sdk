package com.centurylink.cloud.sdk.networks;

import com.centurylink.cloud.sdk.commons.CommonsModule;
import com.centurylink.cloud.sdk.networks.client.NetworksClient;
import com.centurylink.cloud.sdk.networks.services.NetworkService;
import com.google.inject.AbstractModule;

/**
 * @author ilya.drabenia
 */
public class NetworksModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new CommonsModule());

        bind(NetworksClient.class);
        bind(NetworkService.class);
    }

}

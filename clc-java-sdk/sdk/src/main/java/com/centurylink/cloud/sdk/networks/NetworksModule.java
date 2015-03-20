package com.centurylink.cloud.sdk.networks;

import com.centurylink.cloud.sdk.core.datacenters.DataCentersModule;
import com.centurylink.cloud.sdk.networks.services.NetworkService;
import com.google.inject.AbstractModule;
import sun.net.NetworkClient;

/**
 * @author ilya.drabenia
 */
public class NetworksModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new DataCentersModule());

        bind(NetworkClient.class);
        bind(NetworkService.class);
    }

}

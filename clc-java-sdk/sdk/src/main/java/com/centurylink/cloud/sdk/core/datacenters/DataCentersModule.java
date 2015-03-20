package com.centurylink.cloud.sdk.core.datacenters;

import com.centurylink.cloud.sdk.core.datacenters.client.DataCentersClient;
import com.centurylink.cloud.sdk.core.datacenters.services.DataCenterService;
import com.google.inject.AbstractModule;

/**
 * @author ilya.drabenia
 */
public class DataCentersModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DataCentersClient.class);
        bind(DataCenterService.class);
    }

}

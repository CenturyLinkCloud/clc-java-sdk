package com.centurylink.cloud.sdk.common.services;

import com.centurylink.cloud.sdk.common.services.client.DataCentersClient;
import com.centurylink.cloud.sdk.common.services.client.QueueClient;
import com.centurylink.cloud.sdk.common.services.services.DataCenterService;
import com.google.inject.AbstractModule;

/**
 * @author ilya.drabenia
 */
public class CommonsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(QueueClient.class);

        bind(DataCentersClient.class);
        bind(DataCenterService.class);
    }

}

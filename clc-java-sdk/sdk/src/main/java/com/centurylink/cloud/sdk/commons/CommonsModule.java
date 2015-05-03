package com.centurylink.cloud.sdk.commons;

import com.centurylink.cloud.sdk.commons.client.DataCentersClient;
import com.centurylink.cloud.sdk.commons.client.QueueClient;
import com.centurylink.cloud.sdk.commons.services.DataCenterService;
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

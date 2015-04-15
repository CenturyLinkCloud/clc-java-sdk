package com.centurylink.cloud.sdk.core.commons;

import com.centurylink.cloud.sdk.core.commons.client.DataCentersClient;
import com.centurylink.cloud.sdk.core.commons.client.QueueClient;
import com.centurylink.cloud.sdk.core.commons.services.DataCenterService;
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

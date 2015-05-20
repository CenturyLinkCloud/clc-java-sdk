package com.centurylink.cloud.sdk.common.management;

import com.centurylink.cloud.sdk.common.management.client.DataCentersClient;
import com.centurylink.cloud.sdk.common.management.client.QueueClient;
import com.centurylink.cloud.sdk.common.management.services.DataCenterService;
import com.google.inject.AbstractModule;

/**
 * @author ilya.drabenia
 */
public class CommonManagementModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(QueueClient.class);

        bind(DataCentersClient.class);
        bind(DataCenterService.class);
    }

}

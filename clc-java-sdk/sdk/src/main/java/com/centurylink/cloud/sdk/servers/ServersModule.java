package com.centurylink.cloud.sdk.servers;

import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.services.DataCenterService;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.TemplateService;
import com.centurylink.cloud.sdk.servers.services.domain.group.GroupConverter;
import com.centurylink.cloud.sdk.servers.services.domain.template.TemplateConverter;
import com.google.inject.AbstractModule;

/**
 * @author ilya.drabenia
 */
public class ServersModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ServerClient.class);

        bind(ServerService.class);
        bind(GroupConverter.class);
        bind(GroupService.class);
        bind(TemplateConverter.class);
        bind(TemplateService.class);
        bind(DataCenterService.class);
    }

}

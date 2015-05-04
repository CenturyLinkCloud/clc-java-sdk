package com.centurylink.cloud.sdk.servers;

import com.centurylink.cloud.sdk.common.management.CommonManagementModule;
import com.centurylink.cloud.sdk.networks.NetworksModule;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.TemplateService;
import com.centurylink.cloud.sdk.servers.services.domain.group.GroupConverter;
import com.centurylink.cloud.sdk.servers.services.domain.server.ServerConverter;
import com.google.inject.AbstractModule;

/**
 * @author ilya.drabenia
 */
public class ServersModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ServerClient.class);

        bind(ServerService.class);
        bind(ServerConverter.class);
        bind(GroupConverter.class);
        bind(GroupService.class);
        bind(TemplateService.class);

        install(new CommonManagementModule());
        install(new NetworksModule());
    }

}

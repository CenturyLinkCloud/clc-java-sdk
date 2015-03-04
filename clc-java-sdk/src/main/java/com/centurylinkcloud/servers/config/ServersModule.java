package com.centurylinkcloud.servers.config;

import com.centurylinkcloud.servers.client.ServerClient;
import com.centurylinkcloud.servers.service.GroupService;
import com.centurylinkcloud.servers.service.ServerService;
import com.centurylinkcloud.servers.service.TemplateService;
import com.google.inject.AbstractModule;

/**
 * @author ilya.drabenia
 */
public class ServersModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ServerClient.class);

        bind(ServerService.class);
        bind(GroupService.class);
        bind(TemplateService.class);
    }

}

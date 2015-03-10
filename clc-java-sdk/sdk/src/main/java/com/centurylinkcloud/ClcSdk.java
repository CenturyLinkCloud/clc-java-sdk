package com.centurylinkcloud;

import com.centurylinkcloud.core.auth.config.AuthModule;
import com.centurylinkcloud.core.auth.domain.credentials.CredentialsProvider;
import com.centurylinkcloud.servers.config.ServersModule;
import com.centurylinkcloud.servers.service.ServerService;
import com.google.inject.Guice;
import com.google.inject.Inject;

/**
 * @author ilya.drabenia
 */
public class ClcSdk {
    @Inject
    private ServerService serverService;

    public ClcSdk(CredentialsProvider credentialsProvider) {
        Guice
            .createInjector(
                new AuthModule(credentialsProvider),
                new ServersModule()
            )
            .injectMembers(this);
    }

    public ServerService serverService() {
        return serverService;
    }
}

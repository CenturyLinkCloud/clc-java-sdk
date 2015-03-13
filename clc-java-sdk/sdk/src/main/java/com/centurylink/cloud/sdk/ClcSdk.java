package com.centurylink.cloud.sdk;

import com.centurylink.cloud.sdk.core.auth.config.AuthModule;
import com.centurylink.cloud.sdk.core.auth.domain.credentials.CredentialsProvider;
import com.centurylink.cloud.sdk.servers.config.ServersModule;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.TemplateService;
import com.google.inject.Guice;
import com.google.inject.Inject;

/**
 * @author ilya.drabenia
 */
public class ClcSdk {
    @Inject
    ServerService serverService;

    @Inject
    GroupService groupService;

    @Inject
    TemplateService templateService;

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

    public GroupService groupService() {
        return groupService;
    }

    public TemplateService templateService() {
        return templateService;
    }
}

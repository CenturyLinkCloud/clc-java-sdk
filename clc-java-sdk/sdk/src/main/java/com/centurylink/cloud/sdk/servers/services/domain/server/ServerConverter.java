package com.centurylink.cloud.sdk.servers.services.domain.server;

import com.centurylink.cloud.sdk.servers.client.domain.server.CreateServerRequest;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.TemplateService;
import com.google.inject.Inject;

/**
 * @author ilya.drabenia
 */
public class ServerConverter {
    private final GroupService groupService;
    private final TemplateService templateService;

    @Inject
    public ServerConverter(GroupService groupService, TemplateService templateService) {
        this.groupService = groupService;
        this.templateService = templateService;
    }

    public CreateServerRequest buildCreateServerRequest(CreateServerCommand newServer) {
        return
            new CreateServerRequest()
                .name(newServer.getName())
                .cpu(newServer.getMachine().getCpuCount())
                .memoryGB(newServer.getMachine().getRam())
                .password(newServer.getPassword())
                .groupId(
                    groupService
                        .findByRef(newServer.getGroup())
                        .getId()
                )
                .type(ServerType.STANDARD.getCode())
                .sourceServerId(
                    templateService
                        .findByRef(newServer.getTemplate())
                        .getName()
                )
                .primaryDns(newServer.getNetwork().getPrimaryDns())
                .secondaryDns(newServer.getNetwork().getSecondaryDns());
    }

}

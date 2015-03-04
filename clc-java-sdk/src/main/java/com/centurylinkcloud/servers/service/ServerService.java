package com.centurylinkcloud.servers.service;

import com.centurylinkcloud.servers.client.ServerClient;
import com.centurylinkcloud.servers.client.domain.CreateServerCommand;
import com.centurylinkcloud.servers.client.domain.group.GetGroupResult;
import com.centurylinkcloud.servers.domain.InstanceType;
import com.centurylinkcloud.servers.domain.Server;
import com.google.inject.Inject;

/**
 * @author ilya.drabenia
 */
public class ServerService {
    private final GroupService groupService;
    private final TemplateService templateService;
    private final ServerClient client;

    @Inject
    public ServerService(GroupService groupService, TemplateService templateService, ServerClient client) {
        this.groupService = groupService;
        this.templateService = templateService;
        this.client = client;
    }

    public void create(String alias, Server newServer) {
        client
            .create(alias, new CreateServerCommand()
                .name(newServer.getName())
                .sourceServerId(newServer.getTemplate().getName())
                .cpu(newServer.getMachine().getCpuCount())
                .memoryGB(newServer.getMachine().getRam())
                .groupId(
                    groupService
                        .resolve(alias, newServer.getGroup())
                        .getId()
                )
                .type(InstanceType.STANDARD.getCode())
                .sourceServerId(newServer.getTemplate().getName())
            );
    }

}

package com.centurylinkcloud.servers.service;

import com.centurylinkcloud.servers.client.ServerClient;
import com.centurylinkcloud.servers.client.domain.server.CreateServerCommand;
import com.centurylinkcloud.servers.client.domain.server.CreateServerResult;
import com.centurylinkcloud.servers.client.domain.server.GetServerResult;
import com.centurylinkcloud.servers.domain.Response;
import com.centurylinkcloud.servers.domain.ServerType;
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

    public Response<Server> create(Server newServer) {
        CreateServerResult response = client
            .create(new CreateServerCommand()
                .name(newServer.getName())
                .sourceServerId(newServer.getTemplate().getName())
                .cpu(newServer.getMachine().getCpuCount())
                .memoryGB(newServer.getMachine().getRam())
                .groupId(
                    groupService
                        .resolve(newServer.getGroup())
                        .getId()
                )
                .type(ServerType.STANDARD.getCode())
                .sourceServerId(
                    templateService
                        .resolve(newServer.getGroup().getDatacenter(), newServer.getTemplate())
                        .getName()
                )
            );

        GetServerResult serverInfo = client
            .findServerByUuid(response.findServerUuid());

        return new Response<>(
            newServer.id(serverInfo.getId()),
            response.findStatusId(),
            client
        );
    }

    public Server delete(Server server) {
        client.delete(server.getId());
        return server;
    }

}

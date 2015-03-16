package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.server.CreateServerCommand;
import com.centurylink.cloud.sdk.servers.client.domain.server.CreateServerResponse;
import com.centurylink.cloud.sdk.servers.client.domain.server.GetServerResult;
import com.centurylink.cloud.sdk.servers.services.domain.Response;
import com.centurylink.cloud.sdk.servers.services.domain.ServerType;
import com.centurylink.cloud.sdk.servers.services.domain.Server;
import com.google.inject.Inject;

/**
 * @author ilya.drabenia
 */
public class ServerService {
    private final GroupService groupService;
    private final TemplateService templateService;
    private final ServerClient client;
    private final DataCenterService dataCenterService;

    @Inject
    public ServerService(GroupService groupService, TemplateService templateService, ServerClient client,
                         DataCenterService dataCenterService) {
        this.groupService = groupService;
        this.templateService = templateService;
        this.client = client;
        this.dataCenterService = dataCenterService;
    }

    public Response<Server> create(Server newServer) {
        newServer.getGroup().dataCenter(
            dataCenterService.resolveId(newServer.getGroup().getDataCenter())
        );

        CreateServerResponse response = client
            .create(new CreateServerCommand()
                .name(newServer.getName())
                .sourceServerId(newServer.getTemplate().getName())
                .cpu(newServer.getMachine().getCpuCount())
                .memoryGB(newServer.getMachine().getRam())
                .groupId(
                    groupService
                        .resolveId(newServer.getGroup())
                        .getId()
                )
                .type(ServerType.STANDARD.getCode())
                .sourceServerId(
                    templateService
                        .resolveName(newServer.getGroup().getDataCenter(), newServer.getTemplate())
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

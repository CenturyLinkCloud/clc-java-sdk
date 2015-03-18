package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.server.CreateServerRequest;
import com.centurylink.cloud.sdk.servers.client.domain.server.CreateServerResponse;
import com.centurylink.cloud.sdk.servers.client.domain.server.GetServerResult;
import com.centurylink.cloud.sdk.servers.client.domain.server.template.CreateTemplateRequest;
import com.centurylink.cloud.sdk.servers.services.domain.Response;
import com.centurylink.cloud.sdk.servers.services.domain.server.ServerType;
import com.centurylink.cloud.sdk.servers.services.domain.server.CreateServerCommand;
import com.centurylink.cloud.sdk.servers.services.domain.template.CreateTemplateCommand;
import com.centurylink.cloud.sdk.servers.services.domain.template.Template;
import com.google.inject.Inject;

import static com.centurylink.cloud.sdk.servers.services.domain.template.CreateTemplateCommand.Visibility.PRIVATE;

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

    public Response<CreateServerCommand> create(CreateServerCommand newServer) {
        CreateServerResponse response = client
            .create(new CreateServerRequest()
                .name(newServer.getName())
                .sourceServerId(newServer.getTemplate().getName())
                .cpu(newServer.getMachine().getCpuCount())
                .memoryGB(newServer.getMachine().getRam())
                .password(newServer.getPassword())
                .groupId(
                    groupService
                        .resolveRef(newServer.getGroup())
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

    public Response<CreateServerCommand> delete(CreateServerCommand server) {
        CreateServerResponse response = client.delete(server.getId());

        return new Response<>(
            server,
            response.findStatusId(),
            client
        );
    }

    public Response<Template> convertToTemplate(CreateTemplateCommand command) {
        CreateServerResponse response =
            client.convertToTemplate(new CreateTemplateRequest()
                .serverId(command.getServer().getId())
                .description(command.getDescription())
                .visibility(command.getVisibility() == PRIVATE ? "private" : "privateShared")
                .password(command.getPassword())
            );

        return new Response<>(
            new Template()
                .name(response.getServer())
                .description(command.getDescription()),
            response.findStatusId(),
            client
        );
    }

}

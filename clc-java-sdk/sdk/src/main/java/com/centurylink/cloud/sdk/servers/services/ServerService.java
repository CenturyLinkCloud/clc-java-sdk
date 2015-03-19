package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.core.client.errors.ClcServiceException;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.server.CreateServerRequest;
import com.centurylink.cloud.sdk.servers.client.domain.server.CreateServerResponse;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.server.template.CreateTemplateRequest;
import com.centurylink.cloud.sdk.servers.services.domain.Response;
import com.centurylink.cloud.sdk.servers.services.domain.server.ServerType;
import com.centurylink.cloud.sdk.servers.services.domain.server.CreateServerCommand;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.IdServerRef;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.ServerRef;
import com.centurylink.cloud.sdk.servers.services.domain.template.CreateTemplateCommand;
import com.centurylink.cloud.sdk.servers.services.domain.template.Template;
import com.google.common.base.Function;
import com.google.common.util.concurrent.*;
import com.google.inject.Inject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;

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

    public Response<ServerMetadata> create(CreateServerCommand newServer) {
        CreateServerResponse response = client
            .create(new CreateServerRequest()
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
            );

        ServerMetadata serverInfo = client
            .findServerByUuid(response.findServerUuid());

        return new Response<>(
            serverInfo,
            response.findStatusId(),
            client
        );
    }

    public ListenableFuture<Response<ServerMetadata>> createAsync(CreateServerCommand newServer) {
        final SettableFuture<CreateServerResponse> response = client
            .createAsync(new CreateServerRequest()
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
            );

        ListenableFuture<ServerMetadata> metadata =
            Futures.transform(response, new AsyncFunction<CreateServerResponse, ServerMetadata>() {
                @Override
                public ListenableFuture<ServerMetadata> apply(CreateServerResponse input) throws Exception {
                    return client.findServerByUuidAsync(input.findServerUuid());
                }
            });

        return
            Futures.transform(metadata, new Function<ServerMetadata, Response<ServerMetadata>>() {
                @Override
                public Response<ServerMetadata> apply(ServerMetadata serverInfo) {
                    try {
                        return new Response<>(
                            serverInfo,
                            response.get().findStatusId(),
                            client
                        );
                    } catch (InterruptedException | ExecutionException e) {
                        throw new ClcServiceException(e);
                    }
                }
            });
    }

    public Response<ServerRef> delete(ServerRef server) {
        CreateServerResponse response = client.delete(server.as(IdServerRef.class).getId());

        return new Response<>(
            server,
            response.findStatusId(),
            client
        );
    }

    public ServerMetadata findByRef(ServerRef serverRef) {
        return client.findServerById(serverRef.as(IdServerRef.class).getId());
    }

    public Response<Template> convertToTemplate(CreateTemplateCommand command) {
        CreateServerResponse response =
            client.convertToTemplate(new CreateTemplateRequest()
                .serverId(command.getServer().as(IdServerRef.class).getId())
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

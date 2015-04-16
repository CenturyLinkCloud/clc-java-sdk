package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.core.client.ClcClientException;
import com.centurylink.cloud.sdk.core.commons.client.QueueClient;
import com.centurylink.cloud.sdk.core.exceptions.ReferenceNotSupportedException;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.server.BaseServerResponse;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.server.template.CreateTemplateRequest;
import com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.OperationFuture;
import com.centurylink.cloud.sdk.servers.services.domain.server.CreateServerCommand;
import com.centurylink.cloud.sdk.servers.services.domain.server.ServerConverter;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.IdServerRef;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.ServerRef;
import com.centurylink.cloud.sdk.servers.services.domain.template.CreateTemplateCommand;
import com.centurylink.cloud.sdk.servers.services.domain.template.Template;
import com.google.common.base.Function;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.inject.Inject;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import static com.centurylink.cloud.sdk.core.services.predicates.Predicates.*;
import static com.centurylink.cloud.sdk.servers.services.domain.template.CreateTemplateCommand.Visibility.PRIVATE;
import static java.util.stream.Collectors.toList;

/**
 * @author ilya.drabenia
 */
public class ServerService {
    private final ServerConverter serverConverter;
    private final ServerClient client;
    private final QueueClient queueClient;

    @Inject
    public ServerService(ServerConverter serverConverter, ServerClient client, QueueClient queueClient) {
        this.serverConverter = serverConverter;
        this.client = client;
        this.queueClient = queueClient;
    }

    public OperationFuture<ServerMetadata> create(CreateServerCommand command) {
        BaseServerResponse response = client
            .create(serverConverter.buildCreateServerRequest(command));

        ServerMetadata serverInfo = client
            .findServerByUuid(response.findServerUuid());

        return new OperationFuture<>(
            serverInfo,
            response.findStatusId(),
            queueClient
        );
    }

    public ListenableFuture<OperationFuture<ServerMetadata>> createAsync(CreateServerCommand command) {
        final SettableFuture<BaseServerResponse> response =
            client
                .createAsync(
                    serverConverter.buildCreateServerRequest(command)
                );

        ListenableFuture<ServerMetadata> metadata =
            Futures.transform(response, new AsyncFunction<BaseServerResponse, ServerMetadata>() {
                @Override
                public ListenableFuture<ServerMetadata> apply(BaseServerResponse input) throws Exception {
                    return client.findServerByUuidAsync(input.findServerUuid());
                }
            });

        return
            Futures.transform(metadata, new Function<ServerMetadata, OperationFuture<ServerMetadata>>() {
                @Override
                public OperationFuture<ServerMetadata> apply(ServerMetadata serverInfo) {
                    try {
                        return new OperationFuture<>(
                            serverInfo,
                            response.get().findStatusId(),
                            queueClient
                        );
                    } catch (InterruptedException | ExecutionException e) {
                        throw new ClcClientException(e);
                    }
                }
            });
    }

    public OperationFuture<ServerRef> delete(ServerRef server) {
        BaseServerResponse response = client.delete(
            findByRef(server).getId()
        );

        return new OperationFuture<>(
            server,
            response.findStatusId(),
            queueClient
        );
    }

    public ServerMetadata findByRef(ServerRef serverRef) {
        if (serverRef.is(IdServerRef.class)) {
            return client.findServerById(serverRef.as(IdServerRef.class).getId());
        } else {
            throw new ReferenceNotSupportedException(serverRef.getClass());
        }
    }

    public OperationFuture<Template> convertToTemplate(CreateTemplateCommand command) {
        BaseServerResponse response =
            client.convertToTemplate(new CreateTemplateRequest()
                .serverId(command.getServer().as(IdServerRef.class).getId())
                .description(command.getDescription())
                .visibility(command.getVisibility() == PRIVATE ? "private" : "privateShared")
                .password(command.getPassword())
            );

        return new OperationFuture<>(
            new Template()
                .name(response.getServer())
                .description(command.getDescription()),
            response.findStatusId(),
            queueClient
        );
    }

    /**
     * Power on a single server or group of servers
     * @param serverRefs server references list
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> powerOn(ServerRef... serverRefs) {
        return powerOperationResponse(
            client.powerOn(ids(serverRefs))
        );
    }

    /**
     * Power off a single server or group of servers
     * @param serverRefs server references list
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> powerOff(ServerRef... serverRefs) {
        return powerOperationResponse(
            client.powerOff(ids(serverRefs))
        );
    }

    /**
     * Start maintenance mode on a single server or group of servers
     * @param serverRefs server references list
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> startMaintenance(ServerRef... serverRefs) {
        return powerOperationResponse(
            client.startMaintenance(ids(serverRefs))
        );
    }

    /**
     * Stop maintenance mode on a single server or group of servers
     * @param serverRefs server references list
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> stopMaintenance(ServerRef... serverRefs) {
        return powerOperationResponse(
            client.stopMaintenance(ids(serverRefs))
        );
    }

    /**
     * Pause a single server or group of servers
     * @param serverRefs server references list
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> pause(ServerRef... serverRefs) {
        return powerOperationResponse(
            client.pause(ids(serverRefs))
        );
    }

    /**
     * Reboot a single server or group of servers
     * @param serverRefs server references list
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> reboot(ServerRef... serverRefs) {
        return powerOperationResponse(
            client.reboot(ids(serverRefs))
        );
    }

    /**
     * Reset a single server or group of servers
     * @param serverRefs server references list
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> reset(ServerRef... serverRefs) {
        return powerOperationResponse(
            client.reset(ids(serverRefs))
        );
    }

    /**
     * Shut down a single server or group of servers
     * @param serverRefs server references list
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> shutDown(ServerRef... serverRefs) {
        return powerOperationResponse(
            client.shutDown(ids(serverRefs))
        );
    }

    private List<String> ids(ServerRef... serverRefs) {
        return
            Stream
                .of(serverRefs)
                .filter(notNull())
                .map(this::findByRef)
                .map(ServerMetadata::getName)
                .collect(toList());
    }

    private OperationFuture<List<BaseServerResponse>> powerOperationResponse(List<BaseServerResponse> apiResponse) {
        return
            new OperationFuture<>(
                apiResponse,
                apiResponse
                    .stream()
                    .filter(notNull())
                    .map(BaseServerResponse::findStatusId)
                    .collect(toList()),
                queueClient
            );
    }

}
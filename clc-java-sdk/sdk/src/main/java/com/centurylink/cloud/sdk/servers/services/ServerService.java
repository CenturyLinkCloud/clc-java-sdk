package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.core.client.ClcClientException;
import com.centurylink.cloud.sdk.core.client.domain.Link;
import com.centurylink.cloud.sdk.core.commons.client.QueueClient;
import com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.OperationFuture;
import com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.job.SequentialJobsFuture;
import com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.job.SingleJobFuture;
import com.centurylink.cloud.sdk.core.services.ResourceNotFoundException;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.server.BaseServerResponse;
import com.centurylink.cloud.sdk.servers.client.domain.server.CreateSnapshotRequest;
import com.centurylink.cloud.sdk.servers.client.domain.server.PublicIpAddressResponse;
import com.centurylink.cloud.sdk.servers.client.domain.server.RestoreServerRequest;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.server.template.CreateTemplateRequest;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.GroupRef;
import com.centurylink.cloud.sdk.servers.client.domain.ip.PublicIpMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.server.CreateServerCommand;
import com.centurylink.cloud.sdk.servers.services.domain.server.ServerConverter;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;
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

import static com.centurylink.cloud.sdk.core.services.function.Predicates.isAlwaysTruePredicate;
import static com.centurylink.cloud.sdk.core.services.function.Predicates.notNull;
import static com.centurylink.cloud.sdk.servers.services.domain.template.CreateTemplateCommand.Visibility.PRIVATE;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * @author ilya.drabenia
 */
public class ServerService {
    private final ServerConverter serverConverter;
    private final GroupService groupService;
    private final ServerClient client;
    private final QueueClient queueClient;

    @Inject
    public ServerService(ServerConverter serverConverter, ServerClient client, QueueClient queueClient,
                         GroupService groupService) {
        this.serverConverter = serverConverter;
        this.client = client;
        this.queueClient = queueClient;
        this.groupService = groupService;
    }

    public OperationFuture<ServerMetadata> create(CreateServerCommand command) {
        BaseServerResponse response = client
            .create(serverConverter.buildCreateServerRequest(command));

        ServerMetadata serverInfo = client
            .findServerByUuid(response.findServerUuid());

        if (command.getNetwork().getPublicIpAddressRequest() == null) {
            return new OperationFuture<>(
                serverInfo,
                response.findStatusId(),
                queueClient
            );
        } else {
            return new OperationFuture<>(
                serverInfo,
                new SequentialJobsFuture(
                    () -> new SingleJobFuture(response.findStatusId(), queueClient),
                    () ->
                        new OperationFuture<ServerRef>(addPublicIp(
                            serverInfo.asRefById(),
                            command.getNetwork().getPublicIpAddressRequest()
                        ),"" , queueClient)
                            .jobFuture()

                )
            );
        }
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
        BaseServerResponse response = client.delete(idByRef(server));

        return new OperationFuture<>(
            server,
            response.findStatusId(),
            queueClient
        );
    }

    public ServerMetadata findByRef(ServerRef serverRef) {
        return
            findLazy(
                serverRef.asFilter()
            )
            .findFirst().orElseThrow(() ->
                            new ResourceNotFoundException("Server by reference %s not found", serverRef.toString())
            );
    }

    String idByRef(ServerRef ref) {
        if (ref.is(IdServerRef.class)) {
            return ref.as(IdServerRef.class).getId();
        } else {
            return findByRef(ref).getId();
        }
    }

    public Stream<ServerMetadata> findLazy(ServerFilter serverFilter) {
        if (isAlwaysTruePredicate(serverFilter.getPredicate())
            && isAlwaysTruePredicate(serverFilter.getGroupFilter().getPredicate())
            && isAlwaysTruePredicate(serverFilter.getGroupFilter().getDataCenterFilter().getPredicate())) {
            return
                serverFilter
                    .getServerIds()
                    .stream()
                    .map(client::findServerById);
        } else {
            return
                groupService
                    .findLazy(serverFilter.getGroupFilter())
                    .flatMap(group -> group.getAllServers().stream())
                    .filter(serverFilter.getPredicate());
        }
    }

    public List<ServerMetadata> find(ServerFilter serverFilter) {
        return findLazy(serverFilter).collect(toList());
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

    /**
     *  Archive a single server or group of servers
     * @param serverRefs server references list
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> archive(ServerRef... serverRefs) {
        return powerOperationResponse(
            client.archive(ids(serverRefs))
        );
    }

    /**
     * Create snapshot of a single server or group of servers
     * @param expirationDays expiration days (must be between 1 and 10)
     * @param serverRefs server references list
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> createSnapshot(Integer expirationDays, ServerRef... serverRefs) {
        return powerOperationResponse(
                client.createSnapshot(
                        new CreateSnapshotRequest()
                                .snapshotExpirationDays(expirationDays)
                                .serverIds(ids(serverRefs))
                )
        );
    }

    /**
     * Restore a given archived server to a specified group
     * @param server server reference
     * @param group group reference
     * @return OperationFuture wrapper for BaseServerResponse
     */
    public OperationFuture<Link> restore(ServerRef server, GroupRef group) {
        return baseServerResponse(
                client.restore(
                        idByRef(server),
                        new RestoreServerRequest()
                                .targetGroupId(
                                        groupService.findByRef(group).getId()
                                )
                )
        );
    }

    private List<String> ids(ServerRef... serverRefs) {
        return
            Stream
                .of(serverRefs)
                .filter(notNull())
                .map(this::idByRef)
                .map(String::toUpperCase)
                .collect(toList());
    }

    /**
     * Add public IP to server
     * @param serverRef server reference
     * @param publicIpAddressRequest
     * @return server reference
     */
    public ServerRef addPublicIp(ServerRef serverRef, PublicIpMetadata publicIpAddressRequest) {
        client.addPublicIp(idByRef(serverRef), publicIpAddressRequest);
        return serverRef;
    }

    /**
     * Get public IP object
     * @param serverRef server reference
     * @param publicIp existing public IP address
     * @return public IP object
     */
    public PublicIpAddressResponse getPublicIp(ServerRef serverRef, String publicIp) {
        return client.getPublicIp(idByRef(serverRef), publicIp);
    }

    /**
     * Remove public IP from server
     * @param serverRef server reference
     * @param publicIp existing public IP address
     * @return server reference
     */
    public ServerRef removePublicIp(ServerRef serverRef, String publicIp) {
        checkNotNull(publicIp, "publicIp must be not null");
        client.removePublicIp(idByRef(serverRef), publicIp);
        return serverRef;
    }

    /**
     * Remove all public IPs from server
     * @param serverRef server reference
     * @return server reference
     */
    public ServerRef removePublicIp(ServerRef serverRef) {
        ServerMetadata serverMetadata = findByRef(serverRef);
        String serverId = serverMetadata.getId();
        serverMetadata.getDetails().getIpAddresses().parallelStream().forEach(address -> {
            if (address.getPublicIp() != null) {
                client.removePublicIp(serverId, address.getPublicIp());
            }
        });

        return serverRef;
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

    private OperationFuture<Link> baseServerResponse(Link response) {
        return new OperationFuture<>(
            response,
            response.getId(),
            queueClient
        );
    }
}
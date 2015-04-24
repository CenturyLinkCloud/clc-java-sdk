package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.core.client.ClcClientException;
import com.centurylink.cloud.sdk.core.client.domain.Link;
import com.centurylink.cloud.sdk.core.commons.client.QueueClient;
import com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.OperationFuture;
import com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.job.*;
import com.centurylink.cloud.sdk.core.services.ResourceNotFoundException;
import com.centurylink.cloud.sdk.core.services.filter.Filters;
import com.centurylink.cloud.sdk.core.services.function.Streams;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.server.*;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.server.template.CreateTemplateRequest;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.GroupRef;
import com.centurylink.cloud.sdk.servers.services.domain.ip.PublicIpConfig;
import com.centurylink.cloud.sdk.servers.services.domain.ip.PublicIpConverter;
import com.centurylink.cloud.sdk.servers.services.domain.server.CreateServerCommand;
import com.centurylink.cloud.sdk.servers.services.domain.server.ServerConverter;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;
import com.centurylink.cloud.sdk.servers.services.domain.server.future.CreateServerJobFuture;
import com.centurylink.cloud.sdk.servers.services.domain.server.future.PauseServerJobFuture;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.IdServerRef;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.ServerRef;
import com.centurylink.cloud.sdk.servers.services.domain.template.CreateTemplateCommand;
import com.centurylink.cloud.sdk.servers.services.domain.template.Template;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.centurylink.cloud.sdk.core.services.filter.Filters.nullable;
import static com.centurylink.cloud.sdk.core.services.function.Predicates.isAlwaysTruePredicate;
import static com.centurylink.cloud.sdk.core.services.function.Predicates.notNull;
import static com.centurylink.cloud.sdk.servers.services.domain.template.CreateTemplateCommand.Visibility.PRIVATE;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

/**
 * @author ilya.drabenia
 */
public class ServerService {
    private final ServerConverter serverConverter;
    private final GroupService groupService;
    private final ServerClient client;
    private final QueueClient queueClient;
    private final PublicIpConverter publicIpConverter;

    @Inject
    public ServerService(ServerConverter serverConverter, ServerClient client, QueueClient queueClient,
                         GroupService groupService, PublicIpConverter publicIpConverter) {
        this.serverConverter = serverConverter;
        this.client = client;
        this.queueClient = queueClient;
        this.groupService = groupService;
        this.publicIpConverter = publicIpConverter;
    }

    public OperationFuture<ServerMetadata> create(CreateServerCommand command) {
        BaseServerResponse response = client
            .create(serverConverter.buildCreateServerRequest(command));

        ServerMetadata serverInfo = client
            .findServerByUuid(response.findServerUuid());

        return new OperationFuture<>(
            serverInfo,
            new SequentialJobsFuture(
                () ->
                    new CreateServerJobFuture(response.findStatusId(), serverInfo.getId(), queueClient, client),

                () ->
                    addPublicIpIfNeeded(command, serverInfo)
            )
        );
    }

    private JobFuture addPublicIpIfNeeded(CreateServerCommand command, ServerMetadata serverInfo) {
        if (command.getNetwork().getPublicIpConfig() != null) {
            return
                addPublicIp(
                    serverInfo.asRefById(),
                    command.getNetwork().getPublicIpConfig()
                )
                .jobFuture();
        } else {
            return
                new NoWaitingJobFuture();
        }
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
                    .map(nullable(client::findServerById))
                    .filter(notNull());
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
     *
     * @param serverRefs server references list
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> powerOn(ServerRef... serverRefs) {
        return powerOperationResponse(
            client.powerOn(ids(serverRefs))
        );
    }

    /**
     * Power on a single server or group of servers
     *
     * @param serverFilter search servers criteria
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> powerOn(ServerFilter serverFilter) {
        return powerOperationResponse(
            client.powerOn(ids(serverFilter))
        );
    }

    /**
     * Power off a single server or group of servers
     *
     * @param serverRefs server references list
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> powerOff(ServerRef... serverRefs) {
        return powerOperationResponse(
            client.powerOff(ids(serverRefs))
        );
    }

    /**
     * Power off a single server or group of servers
     *
     * @param serverFilter search servers criteria
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> powerOff(ServerFilter serverFilter) {
        return powerOperationResponse(
            client.powerOff(ids(serverFilter))
        );
    }

    /**
     * Start maintenance mode on a single server or group of servers
     *
     * @param serverRefs server references list
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> startMaintenance(ServerRef... serverRefs) {
        return powerOperationResponse(
            client.startMaintenance(ids(serverRefs))
        );
    }

    /**
     * Start maintenance mode on a single server or group of servers
     *
     * @param serverFilter search servers criteria
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> startMaintenance(ServerFilter serverFilter) {
        return powerOperationResponse(
            client.startMaintenance(ids(serverFilter))
        );
    }

    /**
     * Stop maintenance mode on a single server or group of servers
     *
     * @param serverRefs server references list
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> stopMaintenance(ServerRef... serverRefs) {
        return powerOperationResponse(
            client.stopMaintenance(ids(serverRefs))
        );
    }

    /**
     * Stop maintenance mode on a single server or group of servers
     *
     * @param serverFilter search servers criteria
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> stopMaintenance(ServerFilter serverFilter) {
        return powerOperationResponse(
            client.stopMaintenance(ids(serverFilter))
        );
    }

    /**
     * Pause a single server or group of servers
     *
     * @param serverRefs server references list
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> pause(ServerRef... serverRefs) {
        BaseServerListResponse response = client.pause(ids(serverRefs));

        return
            new OperationFuture<>(
                response,
                new ParallelJobsFuture(
                    response.stream().map(r ->
                        new PauseServerJobFuture(
                            r.findStatusId(),
                            r.getServer(),
                            queueClient,
                            client
                        )
                    )
                    .collect(toList())
                )
            );
    }

    /**
     * Pause a single server or group of servers
     *
     * @param serverFilter search servers criteria
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> pause(ServerFilter serverFilter) {
        return powerOperationResponse(
            client.pause(ids(serverFilter))
        );
    }

    /**
     * Reboot a single server or group of servers
     *
     * @param serverRefs server references list
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> reboot(ServerRef... serverRefs) {
        return powerOperationResponse(
            client.reboot(ids(serverRefs))
        );
    }

    /**
     * Pause a single server or group of servers
     *
     * @param serverFilter search servers criteria
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> reboot(ServerFilter serverFilter) {
        return powerOperationResponse(
            client.reboot(ids(serverFilter))
        );
    }

    /**
     * Reset a single server or group of servers
     *
     * @param serverRefs server references list
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> reset(ServerRef... serverRefs) {
        return powerOperationResponse(
            client.reset(ids(serverRefs))
        );
    }

    /**
     * Reset a single server or group of servers
     *
     * @param serverFilter search servers criteria
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> reset(ServerFilter serverFilter) {
        return powerOperationResponse(
            client.reset(ids(serverFilter))
        );
    }

    /**
     * Shut down a single server or group of servers
     *
     * @param serverRefs server references list
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> shutDown(ServerRef... serverRefs) {
        return powerOperationResponse(
            client.shutDown(ids(serverRefs))
        );
    }

    /**
     * Shut down a single server or group of servers
     *
     * @param serverFilter search servers criteria
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> shutDown(ServerFilter serverFilter) {
        return powerOperationResponse(
            client.shutDown(ids(serverFilter))
        );
    }

    /**
     * Archive a single server or group of servers
     *
     * @param serverRefs server references list
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> archive(ServerRef... serverRefs) {
        return powerOperationResponse(
            client.archive(ids(serverRefs))
        );
    }

    /**
     * Archive a single server or group of servers
     *
     * @param serverFilter search servers criteria
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> archive(ServerFilter serverFilter) {
        return powerOperationResponse(
            client.archive(ids(serverFilter))
        );
    }

    /**
     * Create snapshot of a single server or group of servers
     *
     * @param expirationDays expiration days (must be between 1 and 10)
     * @param serverRefs     server references list
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> createSnapshot(Integer expirationDays, ServerRef... serverRefs) {
        return
            powerOperationResponse(
                client.createSnapshot(
                    new CreateSnapshotRequest()
                        .snapshotExpirationDays(expirationDays)
                        .serverIds(ids(serverRefs))
                )
        );
    }

    /**
     * Create snapshot of a single server or group of servers
     *
     * @param expirationDays expiration days (must be between 1 and 10)
     * @param serverFilter search servers criteria
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> createSnapshot(Integer expirationDays, ServerFilter serverFilter) {
        return
            powerOperationResponse(
                client.createSnapshot(
                    new CreateSnapshotRequest()
                        .snapshotExpirationDays(expirationDays)
                        .serverIds(ids(serverFilter))
                )
            );
    }

    /**
     * Restore a given archived server to a specified group
     *
     * @param server server reference
     * @param group  group reference
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

    private List<String> ids(ServerFilter serverFilter) {
        List<ServerMetadata> serverMetadataList = find(serverFilter);

        return
            serverMetadataList
                .stream()
                .filter(notNull())
                .map(ServerMetadata::getId)
                .map(String::toUpperCase)
                .collect(toList());
    }

    /**
     * Add public IP to server
     *
     * @param serverRef        server reference
     * @param publicIpConfig publicIp metadata object
     * @return OperationFuture wrapper for ServerRef
     */
    public OperationFuture<ServerRef> addPublicIp(ServerRef serverRef, PublicIpConfig publicIpConfig) {
        Link response = client.addPublicIp(idByRef(serverRef), publicIpConverter.createPublicIpRequest(publicIpConfig));
        return new OperationFuture<>(
            serverRef,
            response.getId(),
            queueClient
        );
    }

    /**
     * Get public IP object
     *
     * @param serverRef server reference
     * @param publicIp  existing public IP address
     * @return public IP response object
     */
    public PublicIpAddressResponse getPublicIp(ServerRef serverRef, String publicIp) {
        return client.getPublicIp(idByRef(serverRef), publicIp);
    }

    /**
     * Remove public IP from server
     *
     * @param serverRef server reference
     * @param ipAddress  existing public IP address
     * @return OperationFuture wrapper for ServerRef
     */
    public OperationFuture<ServerRef> removePublicIp(ServerRef serverRef, String ipAddress) {
        checkNotNull(ipAddress, "ipAddress must be not null");

        Link response = client.removePublicIp(idByRef(serverRef), ipAddress);

        return new OperationFuture<>(
            serverRef,
            response.getId(),
            queueClient
        );
    }

    /**
     * Remove all public IPs from server
     *
     * @param serverRef server reference
     * @return server reference
     */
    public OperationFuture<ServerRef> removePublicIp(ServerRef serverRef) {
        ServerMetadata serverMetadata = findByRef(serverRef);
        List<JobFuture> jobFutures = new ArrayList<>();
        serverMetadata.getDetails().getIpAddresses()
            .stream()
            .map(IpAddress::getPublicIp)
            .filter(notNull())
            .forEach(address -> jobFutures.add(removePublicIp(serverRef, address).jobFuture()));

        return new OperationFuture<>(
            serverRef,
            new ParallelJobsFuture(jobFutures)
        );
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
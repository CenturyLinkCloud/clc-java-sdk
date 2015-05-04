package com.centurylink.cloud.sdk.servers.services.domain.server;

import com.centurylink.cloud.sdk.common.management.client.domain.datacenters.deployment.capabilities.TemplateMetadata;
import com.centurylink.cloud.sdk.networks.services.NetworkService;
import com.centurylink.cloud.sdk.servers.client.domain.server.CreateServerRequest;
import com.centurylink.cloud.sdk.servers.client.domain.server.DiskRequest;
import com.centurylink.cloud.sdk.servers.client.domain.server.ModifyServerRequest;
import com.centurylink.cloud.sdk.servers.client.domain.server.Password;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.TemplateService;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ilya.drabenia
 */
public class ServerConverter {
    private final GroupService groupService;
    private final TemplateService templateService;
    private final NetworkService networkService;

    @Inject
    public ServerConverter(GroupService groupService, TemplateService templateService,
                           NetworkService networkService) {
        this.groupService = groupService;
        this.templateService = templateService;
        this.networkService = networkService;
    }

    public CreateServerRequest buildCreateServerRequest(CreateServerConfig newServer) {
        TemplateMetadata templateMetadata = templateService.findByRef(newServer.getTemplate());
        return
            new CreateServerRequest()
                .name(newServer.getName())
                .cpu(newServer.getMachine().getCpuCount())
                .memoryGB(newServer.getMachine().getRam())
                .additionalDisks(buildDiskRequestList(newServer.getMachine().getDisks()))
                .password(newServer.getPassword())
                .groupId(
                    groupService
                        .findByRef(newServer.getGroup())
                        .getId()
                )
                .type(ServerType.STANDARD.getCode())
                .sourceServerId(templateMetadata.getName())
                .primaryDns(newServer.getNetwork().getPrimaryDns())
                .secondaryDns(newServer.getNetwork().getSecondaryDns())
                .networkId(
                    (newServer.getNetwork().getNetwork() == null) ? null :
                        networkService
                            .findByRef(newServer.getNetwork().getNetwork())
                            .getNetworkId()
                )
                .timeToLive(newServer.getTimeToLive())
                .managedOS(
                    newServer.isManagedOS(),
                    templateMetadata.hasCapability(TemplateMetadata.MANAGED_OS_VALUE)
                );
    }

    public List<ModifyServerRequest> buildModifyServerRequest(ModifyServerConfig serverConfig) {
        List<ModifyServerRequest> result = new ArrayList<>();

        CredentialsConfig credentialsConfig = serverConfig.getCredentialsConfig();

        if (credentialsConfig.getNewPassword() != null
            && !credentialsConfig.getNewPassword().equals(credentialsConfig.getOldPassword())) {

            result.add(
                new ModifyServerRequest<Password>()
                    .member("password")
                    .value(
                        new Password()
                            .current(credentialsConfig.getOldPassword())
                            .password(credentialsConfig.getNewPassword())
                    )
            );
        }

        if (serverConfig.getDescription() != null) {
            result.add(
                new ModifyServerRequest<String>()
                    .member("description")
                    .value(serverConfig.getDescription())
            );
        }

        if (serverConfig.getGroupId() != null) {
            result.add(
                new ModifyServerRequest<String>()
                    .member("groupId")
                    .value(serverConfig.getGroupId())
            );
        }

        Machine machineConfig = serverConfig.getMachineConfig();

        if (machineConfig.getCpuCount() != null) {
            result.add(
                new ModifyServerRequest<String>()
                    .member("cpu")
                    .value(String.valueOf(machineConfig.getCpuCount()))
            );
        }

        if (machineConfig.getRam() != null) {
            result.add(
                new ModifyServerRequest<String>()
                    .member("memory")
                    .value(String.valueOf(machineConfig.getRam()))
            );
        }

        if (!machineConfig.getDisks().isEmpty()) {
            result.add(
                new ModifyServerRequest<List<DiskConfig>>()
                    .member("disks")
                    .value(machineConfig.getDisks())
            );
        }

        return result;
    }

    public DiskRequest buildDisk(DiskConfig diskConfig) {
        return
            new DiskRequest()
                .type(diskConfig.getDiskType() == DiskType.RAW ? "raw" : "partitioned")
                .path(diskConfig.getPath())
                .sizeGB(diskConfig.getSize());
    }

    public List<DiskRequest> buildDiskRequestList(List<DiskConfig> disks) {
        List<DiskRequest> requests = new ArrayList<>();

        for (DiskConfig curDiskConfig : disks) {
            requests.add(buildDisk(curDiskConfig));
        }

        return requests;
    }

}

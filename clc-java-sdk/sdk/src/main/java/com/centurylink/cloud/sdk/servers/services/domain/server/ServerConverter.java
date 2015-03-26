package com.centurylink.cloud.sdk.servers.services.domain.server;

import com.centurylink.cloud.sdk.networks.services.NetworkService;
import com.centurylink.cloud.sdk.servers.client.domain.server.CreateServerRequest;
import com.centurylink.cloud.sdk.servers.client.domain.server.DiskRequest;
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
    public ServerConverter(GroupService groupService, TemplateService templateService, NetworkService networkService) {
        this.groupService = groupService;
        this.templateService = templateService;
        this.networkService = networkService;
    }

    public CreateServerRequest buildCreateServerRequest(CreateServerCommand newServer) {
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
                .sourceServerId(
                    templateService
                        .findByRef(newServer.getTemplate())
                        .getName()
                )
                .primaryDns(newServer.getNetwork().getPrimaryDns())
                .secondaryDns(newServer.getNetwork().getSecondaryDns())
                .networkId(
                    (newServer.getNetwork().getNetwork() == null) ? null :
                        networkService
                            .findByRef(newServer.getNetwork().getNetwork())
                            .getNetworkId()
                );
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

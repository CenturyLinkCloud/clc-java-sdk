/*
 * (c) 2015 CenturyLink Cloud. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
                .description(newServer.getDescription())
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
                new ModifyServerRequest<List<DiskRequest>>()
                    .member("disks")
                    .value(buildDiskRequestList(machineConfig.getDisks()))
            );
        }

        return result;
    }

    public DiskRequest buildDisk(DiskConfig diskConfig) {
        return
            new DiskRequest()
                .diskId(diskConfig.getDiskId())
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

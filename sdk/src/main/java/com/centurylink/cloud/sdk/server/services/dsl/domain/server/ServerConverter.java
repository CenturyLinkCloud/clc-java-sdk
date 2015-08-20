/*
 * (c) 2015 CenturyLink. All Rights Reserved.
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

package com.centurylink.cloud.sdk.server.services.dsl.domain.server;

import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.deployment.capabilities.TemplateMetadata;
import com.centurylink.cloud.sdk.base.services.dsl.DataCenterService;
import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.network.services.dsl.NetworkService;
import com.centurylink.cloud.sdk.server.services.client.ServerClient;
import com.centurylink.cloud.sdk.server.services.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.server.services.client.domain.server.CustomField;
import com.centurylink.cloud.sdk.server.services.client.domain.server.CustomFieldMetadata;
import com.centurylink.cloud.sdk.server.services.client.domain.server.DiskRequest;
import com.centurylink.cloud.sdk.server.services.client.domain.server.ModifyServerRequest;
import com.centurylink.cloud.sdk.server.services.dsl.GroupService;
import com.centurylink.cloud.sdk.server.services.dsl.TemplateService;
import com.centurylink.cloud.sdk.server.services.client.domain.server.CreateServerRequest;
import com.centurylink.cloud.sdk.server.services.client.domain.server.PasswordProvider;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.GroupConfig;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import static com.centurylink.cloud.sdk.core.function.Predicates.notNull;
import static java.util.stream.Collectors.toList;

/**
 * @author ilya.drabenia
 */
public class ServerConverter {
    private final GroupService groupService;
    private final TemplateService templateService;
    private final NetworkService networkService;
    private final DataCenterService dataCenterService;

    @Inject
    public ServerConverter(GroupService groupService, TemplateService templateService,
                           NetworkService networkService, DataCenterService dataCenterService) {
        this.groupService = groupService;
        this.templateService = templateService;
        this.networkService = networkService;
        this.dataCenterService = dataCenterService;
    }

    public CreateServerRequest buildCreateServerRequest(CreateServerConfig newServer,
                                                        List<CustomFieldMetadata> customFieldsMetadata) {
        TemplateMetadata templateMetadata = templateService.findByRef(newServer.getTemplate());
        GroupMetadata groupMetadata = groupService.findByRef(newServer.getGroup());

        if (newServer.getType().equals(ServerType.HYPERSCALE)) {
            newServer.storageType(StorageType.HYPERSCALE);
        }

        List<CustomField> newFields = composeCustomFields(newServer.getCustomFields(), customFieldsMetadata);

        return
            new CreateServerRequest()
                .name(newServer.getName())
                .description(newServer.getDescription())
                .cpu(newServer.getMachine().getCpuCount())
                .memoryGB(newServer.getMachine().getRam())
                .additionalDisks(
                    buildDiskRequestList(
                        newServer.getMachine().getDisks()
                    )
                )
                .password(newServer.getPassword())
                .groupId(groupMetadata.getId())

                .type(
                    newServer.getType().getCode(),
                    templateMetadata.hasCapability(TemplateMetadata.HYPERSCALE_VALUE)
                )

                .storageType(
                    newServer.getStorageType().getCode(),
                    dataCenterService
                        .getDeploymentCapabilities(
                            DataCenter.refById(groupMetadata.getLocationId())
                        )
                        .getSupportsPremiumStorage()
                )

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
                )
                .customFields(newFields.isEmpty() ? null : newFields);
    }

    private List<CustomField> composeCustomFields(List<CustomField> configFields,
                                                  List<CustomFieldMetadata> customFieldsMetadata) {

        if (customFieldsMetadata == null) {
            return new ArrayList<>(0);
        }
        return configFields.stream()
            .map(config -> {
                CustomFieldMetadata found = customFieldsMetadata.stream()
                    .filter(metadata -> metadata.getName().equals(config.getName()) ||
                            metadata.getId().equals(config.getId())
                    )
                    .findFirst()
                    .orElse(null);

                if (found != null) {
                    return new CustomField().id(found.getId()).value(config.getValue());
                }

                return null;
            })
            .filter(notNull())
            .collect(toList());
    }

    public List<ModifyServerRequest> buildModifyServerRequest(ModifyServerConfig serverConfig,
                                                              List<CustomFieldMetadata> customFieldsMetadata) {
        List<ModifyServerRequest> result = new ArrayList<>();

        CredentialsConfig credentialsConfig = serverConfig.getCredentialsConfig();

        if (credentialsConfig.getNewPassword() != null
            && !credentialsConfig.getNewPassword().equals(credentialsConfig.getOldPassword())) {

            result.add(
                new ModifyServerRequest<PasswordProvider>()
                    .member("password")
                    .value(
                        new PasswordProvider()
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

        if (!serverConfig.getCustomFields().isEmpty()) {
            result.add(
                new ModifyServerRequest<List<CustomField>>()
                    .member("customFields")
                    .value(composeCustomFields(serverConfig.getCustomFields(), customFieldsMetadata))
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

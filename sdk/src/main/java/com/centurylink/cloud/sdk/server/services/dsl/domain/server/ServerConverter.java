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
import com.centurylink.cloud.sdk.core.exceptions.ClcException;
import com.centurylink.cloud.sdk.policy.services.dsl.PolicyService;
import com.centurylink.cloud.sdk.server.services.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.server.services.client.domain.server.CloneServerRequest;
import com.centurylink.cloud.sdk.server.services.client.domain.server.CreateServerRequest;
import com.centurylink.cloud.sdk.server.services.client.domain.server.CustomField;
import com.centurylink.cloud.sdk.server.services.client.domain.server.CustomFieldMetadata;
import com.centurylink.cloud.sdk.server.services.client.domain.server.DiskRequest;
import com.centurylink.cloud.sdk.server.services.client.domain.server.ImportServerRequest;
import com.centurylink.cloud.sdk.server.services.client.domain.server.ModifyServerRequest;
import com.centurylink.cloud.sdk.server.services.client.domain.server.PasswordProvider;
import com.centurylink.cloud.sdk.server.services.client.domain.server.ServerCredentials;
import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.GroupService;
import com.centurylink.cloud.sdk.server.services.dsl.TemplateService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import static com.centurylink.cloud.sdk.core.function.Predicates.notNull;
import static java.util.stream.Collectors.toList;

public class ServerConverter {

    private final GroupService groupService;
    private final TemplateService templateService;
    private final DataCenterService dataCenterService;
    private final PolicyService policyService;

    @Inject
    public ServerConverter(
            GroupService groupService,
            TemplateService templateService,
            DataCenterService dataCenterService,
            PolicyService policyService
    ) {
        this.groupService = groupService;
        this.templateService = templateService;
        this.dataCenterService = dataCenterService;
        this.policyService = policyService;
    }

    public CreateServerRequest buildCreateServerRequest(
            CreateServerConfig config,
            List<CustomFieldMetadata> customFieldsMetadata
    ) {
        GroupMetadata groupMetadata = groupService.findByRef(config.getGroup());
        TemplateMetadata templateMetadata = templateService.findByRef(config.getTemplate());

        if (config.getType().equals(ServerType.HYPERSCALE)) {
            config.storageType(StorageType.HYPERSCALE);
        }

        List<CustomField> newFields = composeCustomFields(config.getCustomFields(), customFieldsMetadata);

        return
            new CreateServerRequest()
                .name(config.getName())
                .description(config.getDescription())
                .cpu(config.getMachine().getCpuCount())
                .memoryGB(config.getMachine().getRam())
                .additionalDisks(
                        buildDiskRequestList(
                                config.getMachine().getDisks()
                        )
                )
                .password(config.getPassword())
                .groupId(groupMetadata.getId())

                .type(
                        config.getType().getCode(),
                        templateMetadata.hasCapability(TemplateMetadata.HYPERSCALE_VALUE)
                )

                .storageType(
                        config.getStorageType().getCode(),
                        dataCenterService
                                .getDeploymentCapabilities(
                                        DataCenter.refById(groupMetadata.getLocationId())
                                )
                                .getSupportsPremiumStorage()
                )

                .sourceServerId(templateMetadata.getName())
                .primaryDns(config.getNetwork().getPrimaryDns())
                .secondaryDns(config.getNetwork().getSecondaryDns())

                .networkId(
                        (config.getNetwork().getNetwork() == null) ? null :
                                dataCenterService
                                        .findNetworkByRef(config.getNetwork().getNetwork())
                                        .getNetworkId()
                )

                .timeToLive(config.getTimeToLive())
                .managedOS(
                        config.isManagedOS(),
                        templateMetadata.hasCapability(TemplateMetadata.MANAGED_OS_VALUE)
                )
                .antiAffinityPolicyId((config.getAntiAffinityPolicy()) == null ? null :
                                policyService.antiAffinity().findByRef(config.getAntiAffinityPolicy()).getId()
                )
                .customFields(newFields.isEmpty() ? null : newFields);
    }

    public CloneServerRequest buildCloneServerRequest(
            CloneServerConfig config,
            ServerMetadata serverMetadata,
            ServerCredentials serverCredentials,
            List<CustomFieldMetadata> customFieldsMetadata
    ) {
        if (config.getGroup() == null) {
            config.setGroup(
                Group.refById(serverMetadata.getGroupId())
            );
        }

        CloneServerRequest request = new CloneServerRequest();
        fillBuildServerRequest(config, request, customFieldsMetadata);

        request.setSourceServerId(serverMetadata.getId());
        request.setSourceServerPassword(serverCredentials.getPassword());

        return request;
    }

    public ImportServerRequest buildImportServerRequest(
            ImportServerConfig config,
            List<CustomFieldMetadata> customFieldsMetadata
    ) {
        if (config.getGroup() == null) {
            throw new ClcException("Group does not set in server config");
        }

        ImportServerRequest request = new ImportServerRequest();
        fillBuildServerRequest(config, request, customFieldsMetadata);

        request.setOvfId(config.getOvfId());
        request.setOvfOsType(config.getOvfOsType());

        return request;
    }

    private <T extends CreateServerConfig, V extends CreateServerRequest> void fillBuildServerRequest(
            T config,
            V request,
            List<CustomFieldMetadata> customFieldsMetadata
    ) {
        if (config.getType().equals(ServerType.HYPERSCALE)) {
            config.storageType(StorageType.HYPERSCALE);
        }

        request.setName(config.getName());
        request.setDescription(config.getDescription());

        if (config.getMachine() != null) {
            request.setCpu(config.getMachine().getCpuCount());
            request.setMemoryGB(config.getMachine().getRam());
            request.setAdditionalDisks(
                    buildDiskRequestList(config.getMachine().getDisks())
            );
        }

        GroupMetadata groupMetadata = groupService.findByRef(config.getGroup());

        request.setPassword(config.getPassword());
        request.setGroupId(groupMetadata.getId());

        if (config.getStorageType() != null) {
            request.setStorageType(
                config.getStorageType().getCode(),
                dataCenterService
                    .getDeploymentCapabilities(
                        DataCenter.refById(groupMetadata.getLocationId())
                    )
                    .getSupportsPremiumStorage()
            );
        }

        if (config.getNetwork() != null) {
            NetworkConfig networkConfig = config.getNetwork();

            request.setPrimaryDns(networkConfig.getPrimaryDns());
            request.setSecondaryDns(networkConfig.getSecondaryDns());

            if (networkConfig.getNetwork() != null) {
                request.setNetworkId(
                    dataCenterService
                        .findNetworkByRef(config.getNetwork().getNetwork())
                        .getNetworkId()
                );
            }
        }

        if (config.getTimeToLive() != null) {
            config.setTimeToLive(config.getTimeToLive());
        }

        if (config.isManagedOS()) {
            request.managedOS(config.isManagedOS(), true);
        }

        if (config.getAntiAffinityPolicy() != null) {
            request.antiAffinityPolicyId(
                policyService
                    .antiAffinity()
                    .findByRef(config.getAntiAffinityPolicy())
                    .getId()
            );
        }

        if (!config.getCustomFields().isEmpty()) {
            request.customFields(
                    composeCustomFields(config.getCustomFields(), customFieldsMetadata)
            );
        }

        if (config.getType() != null) {
            request.setType(config.getType().getCode(), true);
        }
    }

    private List<CustomField> composeCustomFields(
            List<CustomField> configFields,
            List<CustomFieldMetadata> customFieldsMetadata
    ) {
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

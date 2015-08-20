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

package com.centurylink.cloud.sdk.server.services.client;

import com.centurylink.cloud.sdk.core.auth.services.BearerAuthentication;
import com.centurylink.cloud.sdk.core.client.AuthenticatedSdkHttpClient;
import com.centurylink.cloud.sdk.core.client.domain.Link;
import com.centurylink.cloud.sdk.core.config.SdkConfiguration;
import com.centurylink.cloud.sdk.server.services.client.domain.group.ClientBillingStats;
import com.centurylink.cloud.sdk.server.services.client.domain.group.CreateGroupRequest;
import com.centurylink.cloud.sdk.server.services.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.server.services.client.domain.group.MonitoringStatisticRequest;
import com.centurylink.cloud.sdk.server.services.client.domain.group.ServerMonitoringStatistics;
import com.centurylink.cloud.sdk.server.services.client.domain.group.UpdateGroupRequest;
import com.centurylink.cloud.sdk.server.services.client.domain.ip.PublicIpMetadata;
import com.centurylink.cloud.sdk.server.services.client.domain.ip.PublicIpRequest;
import com.centurylink.cloud.sdk.server.services.client.domain.server.BaseServerListResponse;
import com.centurylink.cloud.sdk.server.services.client.domain.server.BaseServerResponse;
import com.centurylink.cloud.sdk.server.services.client.domain.server.CreateServerRequest;
import com.centurylink.cloud.sdk.server.services.client.domain.server.CreateSnapshotRequest;
import com.centurylink.cloud.sdk.server.services.client.domain.server.CustomFieldMetadata;
import com.centurylink.cloud.sdk.server.services.client.domain.server.ModifyServerRequest;
import com.centurylink.cloud.sdk.server.services.client.domain.server.RestoreServerRequest;
import com.centurylink.cloud.sdk.server.services.client.domain.server.ServerCredentials;
import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.google.inject.Inject;
import org.apache.http.HttpStatus;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.util.HashMap;
import java.util.List;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

/**
 * @author ilya.drabenia
 */
public class ServerClient extends AuthenticatedSdkHttpClient {

    @Inject
    public ServerClient(BearerAuthentication authFilter, SdkConfiguration config) {
        super(authFilter, config);
    }

    /**
     * Creates a new server. Calls to this operation must include a token acquired
     * from the authentication endpoint. See the Login API for information on acquiring
     * this token.
     *
     * @param request the {@code CreateServerRequest} instance
     * @return current instance
     */
    public BaseServerResponse create(CreateServerRequest request) {
        return
            client("/servers/{accountAlias}")
                .request().post(
                    entity(request, APPLICATION_JSON_TYPE)
                )
                .readEntity(BaseServerResponse.class);
    }

    public BaseServerResponse delete(String serverId) {
        return
            client("/servers/{accountAlias}/{serverId}")
                .resolveTemplate("serverId", serverId)
                .request()
                .delete(BaseServerResponse.class);
    }

    public ServerMetadata findServerByUuid(String uuid) {
        return
            client("/servers/{accountAlias}/{serverId}?uuid=true")
                .resolveTemplate("serverId", uuid)
                .request()
                .get(ServerMetadata.class);
    }

    public ServerMetadata findServerById(String id) {
        return
            client("/servers/{accountAlias}/{serverId}")
                .resolveTemplate("serverId", id)
                .request()
                .get(ServerMetadata.class);
    }

    public Link modify(String serverId, List<ModifyServerRequest> request) {
        return
            client("/servers/{accountAlias}/{serverId}")
                .resolveTemplate("serverId", serverId)
                .request()
                .method("PATCH", entity(request, APPLICATION_JSON_TYPE))
                .readEntity(Link.class);
    }

    public GroupMetadata getGroup(String rootGroupId, boolean includeServerDetails) {
        WebTarget webTarget =
            client("/groups/{accountAlias}/{rootGroupId}")
                .resolveTemplate("rootGroupId", rootGroupId);

        if (includeServerDetails) {
            webTarget = webTarget.queryParam("serverDetail", "detailed");
        }

        return
            webTarget
                .request()
                .get(GroupMetadata.class);
    }

    public GroupMetadata createGroup(CreateGroupRequest createGroupRequest) {
        return
            client("/groups/{accountAlias}")
                .request()
                .post(entity(createGroupRequest, APPLICATION_JSON_TYPE))
                .readEntity(GroupMetadata.class);
    }

    public boolean updateGroup(String groupId, UpdateGroupRequest updateGroupRequest) {
        int responseStatus = client("/groups/{accountAlias}/{groupId}")
            .resolveTemplate("groupId", groupId)
            .request()
            .method("PATCH", entity(updateGroupRequest.getOperations(), APPLICATION_JSON_TYPE))
            .getStatus();
        return responseStatus == HttpStatus.SC_NO_CONTENT;
    }

    public Link deleteGroup(String groupId) {
        return
            client("/groups/{accountAlias}/{groupId}")
                .resolveTemplate("groupId", groupId)
                .request()
                .delete(Link.class);
    }

    public BaseServerListResponse powerOn(List<String> serverIdList) {
        return sendPowerOperationRequest("powerOn", serverIdList);
    }

    public BaseServerListResponse powerOff(List<String> serverIdList) {
        return sendPowerOperationRequest("powerOff", serverIdList);
    }

    public BaseServerListResponse startMaintenance(List<String> serverIdList) {
        return sendPowerOperationRequest("startMaintenance", serverIdList);
    }

    public BaseServerListResponse stopMaintenance(List<String> serverIdList) {
        return sendPowerOperationRequest("stopMaintenance", serverIdList);
    }

    public BaseServerListResponse pause(List<String> serverIdList) {
        return sendPowerOperationRequest("pause", serverIdList);
    }

    public BaseServerListResponse reboot(List<String> serverIdList) {
        return sendPowerOperationRequest("reboot", serverIdList);
    }

    public BaseServerListResponse reset(List<String> serverIdList) {
        return sendPowerOperationRequest("reset", serverIdList);
    }

    public BaseServerListResponse shutDown(List<String> serverIdList) {
        return sendPowerOperationRequest("shutDown", serverIdList);
    }

    public BaseServerListResponse archive(List<String> serverIdList) {
        return sendPowerOperationRequest("archive", serverIdList);
    }

    public BaseServerListResponse createSnapshot(CreateSnapshotRequest request) {
        return
            client("/operations/{accountAlias}/servers/createSnapshot")
                .request()
                .post(entity(request, APPLICATION_JSON_TYPE))
                .readEntity(BaseServerListResponse.class);
    }

    public Link deleteSnapshot(String serverId, String snapshotId) {
        return
            client("/servers/{accountAlias}/{serverId}/snapshots/{snapshotId}")
                .resolveTemplate("serverId", serverId)
                .resolveTemplate("snapshotId", snapshotId)
                .request()
                .delete()
                .readEntity(Link.class);
    }

    public Link revertToSnapshot(String serverId, String snapshotId) {
        return client("/servers/{accountAlias}/{serverId}/snapshots/{snapshotId}/restore")
            .resolveTemplate("serverId", serverId)
            .resolveTemplate("snapshotId", snapshotId)
            .request()
            .post(entity(new HashMap<>(), APPLICATION_JSON_TYPE))
            .readEntity(Link.class);
    }

    public Link restore(String serverId, RestoreServerRequest request) {
        return
            client("/servers/{accountAlias}/{serverId}/restore")
                .resolveTemplate("serverId", serverId)
                .request()
                .post(entity(request, APPLICATION_JSON_TYPE))
                .readEntity(Link.class);
    }

    public ClientBillingStats getGroupBillingStats(String groupId) {
        return
            client("/groups/{accountAlias}/{groupId}/billing")
                .resolveTemplate("groupId", groupId)
                .request()
                .get(ClientBillingStats.class);
    }

    public List<ServerMonitoringStatistics> getMonitoringStatistics(String groupId, MonitoringStatisticRequest request) {
        WebTarget target = client("/groups/{accountAlias}/{groupId}/statistics")
            .resolveTemplate("groupId", groupId);

        if (request.getStart() != null) {
            target = target.queryParam("start", request.getStart());
        }
        if (request.getEnd() != null) {
            target = target.queryParam("end", request.getEnd());
        }
        if (request.getSampleInterval() != null) {
            target = target.queryParam("sampleInterval", request.getSampleInterval());
        }
        if (request.getType() != null) {
            target = target.queryParam("type", request.getType());
        }
        return
            target
            .request()
            .get(new GenericType<List<ServerMonitoringStatistics>>(){});
    }

    private BaseServerListResponse sendPowerOperationRequest(String operationName, List<String> serverIdList) {
        return
            client("/operations/{accountAlias}/servers/" + operationName)
                .request()
                .post(entity(serverIdList, APPLICATION_JSON_TYPE))
                .readEntity(BaseServerListResponse.class);
    }

    public Link addPublicIp(String serverId, PublicIpRequest publicIpRequest) {
        return
            client("/servers/{accountAlias}/{serverId}/publicIPAddresses")
                .resolveTemplate("serverId", serverId)
                .request()
                .post(entity(publicIpRequest, APPLICATION_JSON_TYPE))
                .readEntity(Link.class);
    }

    public PublicIpMetadata getPublicIp(String serverId, String publicIp) {
        return
            client("/servers/{accountAlias}/{serverId}/publicIPAddresses/{publicIp}")
                .resolveTemplate("serverId", serverId)
                .resolveTemplate("publicIp", publicIp)
                .request()
                .get(PublicIpMetadata.class);
    }

    public Link modifyPublicIp(String serverId, String publicIp, PublicIpRequest publicIpRequest) {
        return
            client("/servers/{accountAlias}/{serverId}/publicIPAddresses/{publicIp}")
                .resolveTemplate("serverId", serverId)
                .resolveTemplate("publicIp", publicIp)
                .request()
                .put(entity(publicIpRequest, APPLICATION_JSON_TYPE))
                .readEntity(Link.class);
    }

    public Link removePublicIp(String serverId, String publicIp) {
        return
            client("/servers/{accountAlias}/{serverId}/publicIPAddresses/{publicIp}")
                .resolveTemplate("serverId", serverId)
                .resolveTemplate("publicIp", publicIp)
                .request()
                .delete()
                .readEntity(Link.class);
    }

    public ServerCredentials getServerCredentials(String serverId) {
        return
            client("/servers/{accountAlias}/{serverId}/credentials")
                .resolveTemplate("serverId", serverId)
                .request()
                .get(ServerCredentials.class);
    }

    public List<CustomFieldMetadata> getCustomFields() {
        return
            client("/accounts/{accountAlias}/customFields")
                .request()
                .get(new GenericType<List<CustomFieldMetadata>>() {});
    }
}
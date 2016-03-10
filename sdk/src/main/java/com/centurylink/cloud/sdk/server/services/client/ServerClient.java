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
import com.centurylink.cloud.sdk.core.client.domain.NetworkLink;
import com.centurylink.cloud.sdk.core.config.SdkConfiguration;
import com.centurylink.cloud.sdk.server.services.client.domain.group.*;
import com.centurylink.cloud.sdk.server.services.client.domain.ip.PublicIpMetadata;
import com.centurylink.cloud.sdk.server.services.client.domain.ip.PublicIpRequest;
import com.centurylink.cloud.sdk.server.services.client.domain.network.AddNetworkRequest;
import com.centurylink.cloud.sdk.server.services.client.domain.network.NetworkMetadata;
import com.centurylink.cloud.sdk.server.services.client.domain.server.*;
import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.domain.invoice.InvoiceData;
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

    private static final String SERVER_ID = "serverId";
    private static final String GROUP_ID = "groupId";
    private static final String PUBLIC_IP = "publicIp";

    private static final String SERVER_URL = "/servers/{accountAlias}";
    private static final String SERVER_URL_WITH_ID = SERVER_URL + "/{serverId}";
    private static final String WITH_PUBLIC_IP = "/publicIPAddresses/{publicIp}";



    public ServerClient(BearerAuthentication authFilter, SdkConfiguration config) {
        super(authFilter, config);
    }

    public BaseServerResponse create(CreateServerRequest request) {
        return
            client(SERVER_URL)
                .request().post(
                    entity(request, APPLICATION_JSON_TYPE)
                )
                .readEntity(BaseServerResponse.class);
    }

    public BaseServerResponse clone(CloneServerRequest request) {
        return
            client(SERVER_URL)
                .request()
                .post(entity(request, APPLICATION_JSON_TYPE))
                .readEntity(BaseServerResponse.class);
    }

    public BaseServerResponse importServer(ImportServerRequest request) {
        return
            client("/vmImport/{accountAlias}")
                .request()
                .post(entity(request, APPLICATION_JSON_TYPE))
                .readEntity(BaseServerResponse.class);
    }


    public BaseServerResponse delete(String serverId) {
        return
            client(SERVER_URL_WITH_ID)
                .resolveTemplate(SERVER_ID, serverId)
                .request()
                .delete(BaseServerResponse.class);
    }

    public ServerMetadata findServerByUuid(String uuid) {
        return
            client(SERVER_URL_WITH_ID + "?uuid=true")
                .resolveTemplate(SERVER_ID, uuid)
                .request()
                .get(ServerMetadata.class);
    }

    public ServerMetadata findServerById(String id) {
        return
            client(SERVER_URL_WITH_ID)
                .resolveTemplate(SERVER_ID, id)
                .request()
                .get(ServerMetadata.class);
    }

    public Link modify(String serverId, List<ModifyServerRequest> request) {
        return
            client(SERVER_URL_WITH_ID)
                .resolveTemplate(SERVER_ID, serverId)
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
            .resolveTemplate(GROUP_ID, groupId)
            .request()
            .method("PATCH", entity(updateGroupRequest.getOperations(), APPLICATION_JSON_TYPE))
            .getStatus();
        return responseStatus == HttpStatus.SC_NO_CONTENT;
    }

    public Link deleteGroup(String groupId) {
        return
            client("/groups/{accountAlias}/{groupId}")
                .resolveTemplate(GROUP_ID, groupId)
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
            client(SERVER_URL_WITH_ID + "/snapshots/{snapshotId}")
                .resolveTemplate(SERVER_ID, serverId)
                .resolveTemplate("snapshotId", snapshotId)
                .request()
                .delete()
                .readEntity(Link.class);
    }

    public Link revertToSnapshot(String serverId, String snapshotId) {
        return client(SERVER_URL_WITH_ID + "/snapshots/{snapshotId}/restore")
            .resolveTemplate(SERVER_ID, serverId)
            .resolveTemplate("snapshotId", snapshotId)
            .request()
            .post(entity(new HashMap<>(), APPLICATION_JSON_TYPE))
            .readEntity(Link.class);
    }

    public Link restore(String serverId, RestoreServerRequest request) {
        return
            client(SERVER_URL_WITH_ID + "/restore")
                .resolveTemplate(SERVER_ID, serverId)
                .request()
                .post(entity(request, APPLICATION_JSON_TYPE))
                .readEntity(Link.class);
    }

    public ClientBillingStats getGroupBillingStats(String groupId) {
        return
            client("/groups/{accountAlias}/{groupId}/billing")
                .resolveTemplate(GROUP_ID, groupId)
                .request()
                .get(ClientBillingStats.class);
    }

    public List<ServerMonitoringStatistics> getMonitoringStatistics(String groupId, MonitoringStatisticRequest request) {
        WebTarget target = client("/groups/{accountAlias}/{groupId}/statistics")
            .resolveTemplate(GROUP_ID, groupId);

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
            .get(new GenericType<List<ServerMonitoringStatistics>>() {
            });
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
            client(SERVER_URL_WITH_ID + "/publicIPAddresses")
                .resolveTemplate(SERVER_ID, serverId)
                .request()
                .post(entity(publicIpRequest, APPLICATION_JSON_TYPE))
                .readEntity(Link.class);
    }

    public PublicIpMetadata getPublicIp(String serverId, String publicIp) {
        return
            client(SERVER_URL_WITH_ID + WITH_PUBLIC_IP)
                .resolveTemplate(SERVER_ID, serverId)
                .resolveTemplate(PUBLIC_IP, publicIp)
                .request()
                .get(PublicIpMetadata.class);
    }

    public Link modifyPublicIp(String serverId, String publicIp, PublicIpRequest publicIpRequest) {
        return
            client(SERVER_URL_WITH_ID + WITH_PUBLIC_IP)
                .resolveTemplate(SERVER_ID, serverId)
                .resolveTemplate(PUBLIC_IP, publicIp)
                .request()
                .put(entity(publicIpRequest, APPLICATION_JSON_TYPE))
                .readEntity(Link.class);
    }

    public Link removePublicIp(String serverId, String publicIp) {
        return
            client(SERVER_URL_WITH_ID + WITH_PUBLIC_IP)
                .resolveTemplate(SERVER_ID, serverId)
                .resolveTemplate(PUBLIC_IP, publicIp)
                .request()
                .delete()
                .readEntity(Link.class);
    }

    public ServerCredentials getServerCredentials(String serverId) {
        return
            client(SERVER_URL_WITH_ID + "/credentials")
                .resolveTemplate(SERVER_ID, serverId)
                .request()
                .get(ServerCredentials.class);
    }

    public List<CustomFieldMetadata> getCustomFields() {
        return
            client("/accounts/{accountAlias}/customFields")
                .request()
                .get(new GenericType<List<CustomFieldMetadata>>() {
                });
    }

    public InvoiceData getInvoice(int year, int month, String pricingAccountAlias) {
        return
            client("/invoice/{accountAlias}/{year}/{month}")
                .resolveTemplate("year", year)
                .resolveTemplate("month", month)
                .queryParam("pricingAccountAlias", pricingAccountAlias)
                .request()
                .get(InvoiceData.class);
    }

    public List<NetworkMetadata> getNetworks(String dataCenter) {
        return
            experimentalClient("/networks/{accountAlias}/{dataCenter}")
                .resolveTemplate("dataCenter", dataCenter.toLowerCase())
                .request()
                .get(new GenericType<List<NetworkMetadata>>() {});
    }

    public NetworkMetadata getNetwork(String networkId, String dataCenterId, String ipAddressesDetails) {
        return
            experimentalClient("/networks/{accountAlias}/{dataCenter}/{networkId}")
                .resolveTemplate("dataCenter", dataCenterId.toLowerCase())
                .resolveTemplate("networkId", networkId)
                .queryParam("ipAddresses", ipAddressesDetails.toLowerCase())
                .request()
                .get(NetworkMetadata.class);
    }

    public NetworkLink addSecondaryNetwork(String serverId, AddNetworkRequest networkRequest) {
        return
            client(SERVER_URL_WITH_ID + "/networks")
                .resolveTemplate(SERVER_ID, serverId)
                .request()
                .post(entity(networkRequest, APPLICATION_JSON_TYPE))
                .readEntity(NetworkLink.class);
    }

    public NetworkLink removeSecondaryNetwork(String serverId, String networkId) {
        return
            client(SERVER_URL_WITH_ID + "/networks/{network}")
                .resolveTemplate(SERVER_ID, serverId)
                .resolveTemplate("network", networkId)
                .request()
                .delete()
                .readEntity(NetworkLink.class);
    }
}
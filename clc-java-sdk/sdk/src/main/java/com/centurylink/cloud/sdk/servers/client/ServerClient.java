package com.centurylink.cloud.sdk.servers.client;

import com.centurylink.cloud.sdk.core.auth.services.BearerAuthentication;
import com.centurylink.cloud.sdk.core.client.SdkHttpClient;
import com.centurylink.cloud.sdk.core.client.domain.Link;
import com.centurylink.cloud.sdk.core.config.SdkConfiguration;
import com.centurylink.cloud.sdk.servers.client.domain.group.CreateGroupRequest;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupBillingStats;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.group.UpdateGroupRequest;
import com.centurylink.cloud.sdk.servers.client.domain.ip.PublicIpMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.ip.PublicIpRequest;
import com.centurylink.cloud.sdk.servers.client.domain.server.*;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.server.template.CreateTemplateRequest;
import com.google.inject.Inject;
import org.apache.http.HttpStatus;

import javax.ws.rs.client.WebTarget;
import java.util.HashMap;
import java.util.List;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

/**
 * @author ilya.drabenia
 */
public class ServerClient extends SdkHttpClient {

    @Inject
    public ServerClient(BearerAuthentication authFilter, SdkConfiguration config) {
        super(authFilter, config);
    }

    /**
     * Creates a new server. Calls to this operation must include a token acquired
     * from the authentication endpoint. See the Login API for information on acquiring
     * this token.
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
                    .request().delete(Link.class);
    }

    public BaseServerResponse convertToTemplate(CreateTemplateRequest request) {
        return
            client("/servers/{accountAlias}/{serverId}/convertToTemplate")
                .resolveTemplate("serverId", request.getServerId())
                .request()
                .post(entity(request, APPLICATION_JSON_TYPE))
                .readEntity(BaseServerResponse.class);
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

    public GroupBillingStats getGroupBillingStats(String groupId) {
        return
            client("/groups/{accountAlias}/{groupId}/billing")
                .resolveTemplate("groupId", groupId)
                .request()
                .get(GroupBillingStats.class);
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

}
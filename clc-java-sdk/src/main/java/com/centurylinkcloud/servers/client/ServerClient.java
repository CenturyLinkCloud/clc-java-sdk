package com.centurylinkcloud.servers.client;

import com.centurylinkcloud.auth.BearerAuthentication;
import com.centurylinkcloud.servers.client.domain.GetStatusResult;
import com.centurylinkcloud.servers.client.domain.datacenter.deployment.capabilities.GetDeploymentCapabilitiesResult;
import com.centurylinkcloud.servers.client.domain.server.CreateServerCommand;
import com.centurylinkcloud.servers.client.domain.GetDataCenterResult;
import com.centurylinkcloud.servers.client.domain.group.GetGroupResult;
import com.centurylinkcloud.servers.client.domain.server.CreateServerResult;
import com.centurylinkcloud.servers.client.domain.server.GetServerResult;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import static javax.ws.rs.client.Entity.entity;

/**
 * @author ilya.drabenia
 */
public class ServerClient {

    /**
     * Creates a new server. Calls to this operation must include a token acquired
     * from the authentication endpoint. See the Login API for information on acquiring
     * this token.
     */
    public CreateServerResult create(CreateServerCommand request) {
        return
            client("/servers/{accountAlias}")
                .request().post(
                    entity(request, MediaType.APPLICATION_JSON_TYPE)
                )
                .readEntity(CreateServerResult.class);
    }

    public String delete(String serverId) {
        return
            client("/servers/{accountAlias}/{serverId}")
                .resolveTemplate("serverId", serverId)
                .request()
                .delete(String.class);
    }

    public GetServerResult findServerByUuid(String uuid) {
        return
            client("/servers/{accountAlias}/{serverId}?uuid=true")
                .resolveTemplate("serverId", uuid)
                .request()
                .get(GetServerResult.class);
    }

    public GetDataCenterResult getDataCenter(String dataCenterId) {
        return
            client("/datacenters/{accountAlias}/{dataCenterId}")
                .queryParam("groupLinks", true)
                .resolveTemplate("dataCenterId", dataCenterId)
                .request().get(GetDataCenterResult.class);
    }

    public GetGroupResult getGroups(String rootGroupId) {
        return
            client("/groups/{accountAlias}/{rootGroupId}")
                .resolveTemplate("rootGroupId", rootGroupId)
                .request().get(GetGroupResult.class);
    }

    public GetDeploymentCapabilitiesResult getDataCenterDeploymentCapabilities(String dataCenterId) {
        return
            client("/datacenters/{accountAlias}/{dataCenterId}/deploymentCapabilities")
                .resolveTemplate("dataCenterId", dataCenterId)
                .request().get(GetDeploymentCapabilitiesResult.class);
    }

    public GetStatusResult getJobStatus(String jobId) {
        return
            client("/operations/{accountAlias}/status/{statusId}")
                .resolveTemplate("statusId", jobId)
                .request()
                .get(GetStatusResult.class);
    }

    private WebTarget client(String target) {
        return
            ClientBuilder
                .newBuilder()
                    .register(new BearerAuthentication("idrabenia", "RenVortEr9"))
                .build()
                .target("https://api.tier3.com/v2" + target)
                .resolveTemplate("accountAlias", BearerAuthentication.getAccountAlias());
    }
}

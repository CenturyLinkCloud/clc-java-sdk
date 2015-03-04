package com.centurylinkcloud.servers.client;

import com.centurylinkcloud.auth.BearerAuthentication;
import com.centurylinkcloud.servers.client.domain.CreateServerCommand;
import com.centurylinkcloud.servers.client.domain.GetDataCenterResult;
import com.centurylinkcloud.servers.client.domain.deployment.capabilities.GetDeploymentCapabilitiesResult;
import com.centurylinkcloud.servers.client.domain.group.GetGroupResult;

import javax.ws.rs.client.ClientBuilder;
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
    public String create(String alias, CreateServerCommand request) {
        return
            ClientBuilder.newBuilder().build()
                .register(new BearerAuthentication("sergey.b", "QG0*0!jBcUfNPZ(["))
                .target("https://api.tier3.com/v2/servers/{accountAlias}")
                    .resolveTemplate("accountAlias", alias)

                .request().post(
                    entity(request, MediaType.APPLICATION_JSON_TYPE)
                )
                .readEntity(String.class);
    }

    public GetDataCenterResult getDataCenter(String alias, String dataCenterId) {
        return
            ClientBuilder.newBuilder().build()
                .register(new BearerAuthentication("sergey.b", "QG0*0!jBcUfNPZ(["))
                .target("https://api.tier3.com/v2/datacenters/{accountAlias}/{dataCenterId}")
                .queryParam("groupLinks", true)
                .resolveTemplate("accountAlias", alias)
                .resolveTemplate("dataCenterId", dataCenterId)
                .request().get(GetDataCenterResult.class);
    }

    public GetGroupResult getGroups(String alias, String rootGroupId) {
        return
            ClientBuilder.newBuilder().build()
                .register(new BearerAuthentication("sergey.b", "QG0*0!jBcUfNPZ(["))
                .target("https://api.tier3.com/v2/groups/{accountAlias}/{rootGroupId}")
                .resolveTemplate("accountAlias", alias)
                .resolveTemplate("rootGroupId", rootGroupId)
                .request().get(GetGroupResult.class);
    }

    public GetDeploymentCapabilitiesResult getDataCenterDeploymentCapabilities(String alias, String dataCenterId) {
        return
            ClientBuilder.newBuilder().build()
                .register(new BearerAuthentication("sergey.b", "QG0*0!jBcUfNPZ(["))
                .target("https://api.tier3.com/v2/datacenters/{accountAlias}/{dataCenterId}/deploymentCapabilities")
                .resolveTemplate("accountAlias", alias)
                .resolveTemplate("dataCenterId", dataCenterId)
                .request().get(GetDeploymentCapabilitiesResult.class);
    }

}

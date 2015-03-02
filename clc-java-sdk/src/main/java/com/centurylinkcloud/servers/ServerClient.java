package com.centurylinkcloud.servers;

import com.centurylinkcloud.auth.BearerAuthentication;
import com.centurylinkcloud.servers.model.CreateServerCommand;
import com.centurylinkcloud.servers.model.CreateServerResult;

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

}

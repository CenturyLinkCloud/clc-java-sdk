package com.centurylink.sdk2.api.server;

import com.centurylink.sdk2.api.server.create.CreateServerCommand;
import com.centurylink.sdk2.api.server.create.CreateServerResult;

import static com.centurylink.sdk2.dsl.language.endpoint.EndpointBuilder.endpoint;
import static com.centurylink.sdk2.dsl.language.endpoint.Method.POST;
import static com.centurylink.sdk2.dsl.language.endpoint.PathParamBuilder.pathParam;
import static com.centurylink.sdk2.dsl.language.PrimitiveType.STRING;

/**
 * @author ilya.drabenia
 */
public class ServerService {

    public Object create() {
        return
            endpoint("server::create")
                .desc(
                " Creates a new server. Calls to this operation must include a token acquired",
                " from the authentication endpoint. See the Login API for information on acquiring",
                " this token."
                )
                .url("https://api.tier3.com/v2/servers/{accountAlias}")
                .method(POST)
                .param(pathParam()
                    .name("accountAlias")
                    .desc("Short code for a particular account")
                    .type(STRING)
                    .required(true)
                )
                .request(CreateServerCommand.class)
                .response(CreateServerResult.class)
            .end();
    }

}

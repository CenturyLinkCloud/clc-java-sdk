
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;


public class ServerService {


    /**
     *  Creates a new server. Calls to this operation must include a token acquired
     from the authentication endpoint. See the Login API for information on acquiring
     this token.
     */
    public CreateServerResult create(CreateServerCommand request) {
        return
            ClientBuilder.newBuilder().build()
                .target("http://localhost:8080/server")
                .request()
                .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(CreateServerResult.class);
    }

}
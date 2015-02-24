import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;


public class ServerService {


    public CreateServerResult create(CreateServerCommand request) {
        return
                ClientBuilder.newBuilder().build()
                        .target("http://localhost:8080/server")
                        .request()
                        .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE))
                        .readEntity(CreateServerResult.class);
    }


}
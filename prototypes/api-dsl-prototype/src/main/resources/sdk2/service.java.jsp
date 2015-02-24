
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;


public class ServerService {

    <% for (curMethod in endpoints) { %>
    /**
     * ${curMethod.description}
     */
    public ${curMethod.response.specification().name} ${curMethod.endpointName}(${curMethod.request.specification().name} request) {
        return
            ClientBuilder.newBuilder().build()
                .target("${curMethod.url}")
                .request()
                    .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(${curMethod.response.specification().name}.class);
    }
    <% } %>

}
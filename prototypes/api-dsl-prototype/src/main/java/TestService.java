import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

/**
 * @author ilya.drabenia
 */
public class TestService {

    public static void main(String... args) {
        System.out.println(
            new ServerService()
                .create(new CreateServerCommand()
                    .cpu(10)
                    .groupId("A5")
                    .memoryGB(10)
                    .name("server1")
                    .type(5)
                    .sourceServerId("server0")
                )
                .getResult()
        );
    }

}

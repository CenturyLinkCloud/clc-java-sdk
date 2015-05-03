package com.centurylink.cloud.sdk.servers.services.servers.create;

import com.centurylink.cloud.sdk.base.auth.AuthModule;
import com.centurylink.cloud.sdk.base.client.domain.Link;
import com.centurylink.cloud.sdk.common.services.client.domain.datacenters.deployment.capabilities.TemplateMetadata;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.ServersModule;
import com.centurylink.cloud.sdk.servers.TestModule;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.server.BaseServerResponse;
import com.centurylink.cloud.sdk.servers.client.domain.server.CreateServerRequest;
import com.centurylink.cloud.sdk.servers.client.domain.server.DiskRequest;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.TemplateService;
import com.centurylink.cloud.sdk.servers.services.domain.server.DiskConfig;
import com.centurylink.cloud.sdk.servers.services.domain.server.Machine;
import com.centurylink.cloud.sdk.servers.services.servers.TestServerSupport;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.centurylink.cloud.sdk.servers.services.domain.server.DiskType.PARTITIONED;
import static com.centurylink.cloud.sdk.servers.services.domain.server.DiskType.RAW;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Ilya Drabenia
 */
public class DiskManagementTest extends AbstractServersSdkTest {

    @Inject
    ServerService serverService;

    @Inject
    ServerClient serverClient;

    @Inject
    TemplateService templateService;

    @Inject
    GroupService groupService;

    @Override
    protected List<Module> modules() {
        return list(new AuthModule(), Modules.override(new ServersModule()).with(new TestModule() {
            @Override
            protected void configure() {
                this
                    .mock(ServerClient.class)
                    .mock(TemplateService.class)
                    .mock(GroupService.class);
            }
        }));
    }

    @Test
    public void testCreateServerWithDisksOperation() {
        when(templateService.findByRef(any())).thenReturn(new TemplateMetadata());
        when(groupService.findByRef(any())).thenReturn(new GroupMetadata());
        when(serverClient.findServerByUuid(any())).thenReturn(new ServerMetadata());
        when(serverClient.create(any()))
            .thenReturn(new BaseServerResponse(null, true, new ArrayList<Link>() {{
                add(new Link() {{
                    setRel("status");
                    setId("statusId");
                }});
                add(new Link() {{
                    setRel("self");
                    setId("serverId");
                }});
            }}, null));

        serverService.create(TestServerSupport.anyServerConfig()
            .machine(new Machine()
                .cpuCount(2)
                .ram(4)
                .disk(new DiskConfig()
                    .type(RAW)
                    .size(15)
                )
                .disk(new DiskConfig()
                    .type(PARTITIONED)
                    .path("/d1")
                    .size(20)
                )
            )
        );


        ArgumentCaptor<CreateServerRequest> request = ArgumentCaptor.forClass(CreateServerRequest.class);
        Mockito.verify(serverClient).create(request.capture());
        List<DiskRequest> additionalDisks = request.getValue().getAdditionalDisks();

        assert additionalDisks.size() == 2;
        assert additionalDisks.get(0).getType().equals("raw");
        assert additionalDisks.get(0).getSizeGB() == 15;

        assert additionalDisks.get(1).getType().equals("partitioned");
        assert additionalDisks.get(1).getSizeGB() == 20;
        assert additionalDisks.get(1).getPath().equals("/d1");
    }

}

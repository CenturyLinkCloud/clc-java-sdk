package com.centurylink.cloud.sdk.tests.fixtures;

import com.centurylink.cloud.sdk.core.client.domain.Link;
import com.centurylink.cloud.sdk.core.commons.client.QueueClient;
import com.centurylink.cloud.sdk.core.commons.client.domain.queue.GetStatusResponse;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.ChangeInfo;
import com.centurylink.cloud.sdk.servers.client.domain.server.BaseServerListResponse;
import com.centurylink.cloud.sdk.servers.client.domain.server.BaseServerResponse;
import com.centurylink.cloud.sdk.servers.client.domain.server.CreateSnapshotRequest;
import com.centurylink.cloud.sdk.servers.client.domain.server.Details;
import com.centurylink.cloud.sdk.servers.client.domain.server.RestoreServerRequest;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.IdServerRef;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Server stub with serverService mocked methods
 */
public class ServerStubFixture {

    private final static String serverId = "de1altdtcrt777";
    private final static String serverId2 = "de1altdtcrt888";

    private static Link link;

    private ServerMetadata serverMetadata;
    private ServerMetadata serverMetadata2;

    /* mocked server client*/
    ServerClient serverClient;

    /* mocked queue client*/
    QueueClient queueClient;

    public ServerMetadata getServerMetadata() {
        return serverMetadata;
    }

    public ServerMetadata getAnotherServerMetadata() {
        return serverMetadata2;
    }

    public ServerStubFixture(ServerClient serverClient, QueueClient queueClient) {
        this.serverClient = serverClient;
        this.queueClient = queueClient;

        initMockAndStubs();
    }

    private void initMockAndStubs() {
        serverMetadata = createServerMetadata(serverId);
        serverMetadata2 = createServerMetadata(serverId2);

        link = createLink();

        GetStatusResponse statusResponse = new GetStatusResponse("succeeded");
        when(queueClient.getJobStatus(anyString())).thenReturn(statusResponse);

        List<String> serverIdList = new ArrayList<>();
        serverIdList.add(serverMetadata.getId());
        serverIdList.add(serverMetadata2.getId());

        BaseServerListResponse baseServerListResponse = createBaseServerListResponse();

        when(serverClient.findServerById(serverIdList.get(0)))
            .thenReturn(serverMetadata);

        when(serverClient.findServerById(serverIdList.get(1)))
                .thenReturn(serverMetadata);

        when(serverClient.powerOn(anyListOf(String.class)))
            .thenAnswer(
                new Answer<BaseServerListResponse>() {
                    public BaseServerListResponse answer(InvocationOnMock invocation) {
                        serverMetadata.getDetails().setPowerState("started");
                        serverMetadata2.getDetails().setPowerState("started");
                        return baseServerListResponse;
                    }
                }
            );

        when(serverClient.powerOff(anyListOf(String.class)))
            .thenAnswer(
                new Answer<BaseServerListResponse>() {
                    public BaseServerListResponse answer(InvocationOnMock invocation) {
                        serverMetadata.getDetails().setPowerState("stopped");
                        serverMetadata2.getDetails().setPowerState("stopped");
                        return baseServerListResponse;
                    }
                }
            );

        when(serverClient.startMaintenance(anyListOf(String.class)))
            .thenAnswer(
                new Answer<BaseServerListResponse>() {
                    public BaseServerListResponse answer(InvocationOnMock invocation) {
                        serverMetadata.getDetails().setInMaintenanceMode(true);
                        serverMetadata2.getDetails().setInMaintenanceMode(true);
                        return baseServerListResponse;
                    }
                }
            );

        when(serverClient.stopMaintenance(anyListOf(String.class)))
            .thenAnswer(
                new Answer<BaseServerListResponse>() {
                    public BaseServerListResponse answer(InvocationOnMock invocation) {
                        serverMetadata.getDetails().setInMaintenanceMode(false);
                        serverMetadata2.getDetails().setInMaintenanceMode(false);
                        return baseServerListResponse;
                    }
                }
            );

        when(serverClient.pause(anyListOf(String.class)))
            .thenAnswer(
                new Answer<BaseServerListResponse>() {
                    public BaseServerListResponse answer(InvocationOnMock invocation) {
                        serverMetadata.getDetails().setPowerState("paused");
                        serverMetadata2.getDetails().setPowerState("paused");
                        return baseServerListResponse;
                    }
                }
            );

        when(serverClient.reset(anyListOf(String.class)))
            .thenReturn(baseServerListResponse);

        when(serverClient.reboot(anyListOf(String.class)))
            .thenReturn(baseServerListResponse);

        when(serverClient.archive(anyListOf(String.class)))
            .thenAnswer(
                new Answer<BaseServerListResponse>() {
                    public BaseServerListResponse answer(InvocationOnMock invocation) {
                        serverMetadata.setStatus("archived");
                        serverMetadata2.setStatus("archived");
                        return baseServerListResponse;
                    }
                }
            );

        when(serverClient.restore(anyString(), any(RestoreServerRequest.class)))
            .thenAnswer(
                new Answer<Link>() {
                    public Link answer(InvocationOnMock invocation) {
                        serverMetadata.setStatus("active");
                        serverMetadata2.setStatus("active");
                        return link;
                    }
                }
            );

        when(serverClient.shutDown(anyListOf(String.class)))
            .thenAnswer(
                new Answer<BaseServerListResponse>() {
                    public BaseServerListResponse answer(InvocationOnMock invocation) {
                        serverMetadata.getDetails().setPowerState("stopped");
                        serverMetadata2.getDetails().setPowerState("stopped");
                        return baseServerListResponse;
                    }
                }
            );

        when(serverClient.createSnapshot(any(CreateSnapshotRequest.class)))
            .thenAnswer(
                new Answer<BaseServerListResponse>() {
                    public BaseServerListResponse answer(InvocationOnMock invocation) {
                        serverMetadata.getDetails().getSnapshots().add(new Object());
                        serverMetadata2.getDetails().getSnapshots().add(new Object());
                        return baseServerListResponse;
                    }
                }
            );
    }

    private ServerMetadata createServerMetadata(String serverId) {
        return
            new ServerMetadata() {{
                setId(serverId);
                setName(serverId.toUpperCase());
                setGroupId("de10392941c64838a4f0daec38e67f5a");
                setIsTemplate(false);
                setLocationId("DE1");
                setOsType("CentOS 5 64-bit");
                setOs("centOS5_64Bit");
                setStatus("active");
                setType("standard");
                setStorageType("premium");
                setChangeInfo(new ChangeInfo() {{
                    setCreatedBy("idrabenia.altd");
                    setCreatedDate("2015-04-23T11:41:22Z");
                    setModifiedBy("idrabenia.altd");
                    setModifiedDate("2015-04-23T11:48:40Z");
                }});
                setDetails(new Details() {{
                    setCpu(1);
                    setDiskCount(4);
                    setMemoryMB(3072);
                    setPowerState("started");
                    setInMaintenanceMode(false);
                }});
            }};
    }

    private Link createLink() {
        return
            new Link() {{
                setRel("status");
                setHref("/v2/operations/alias/status/wa1-12345");
                setId("wa1-12345");
            }};
    }

    private BaseServerListResponse createBaseServerListResponse() {
        BaseServerListResponse responseList = new BaseServerListResponse();

        List<Link> linkList = new ArrayList<>();
        linkList.add(link);

        BaseServerResponse response = new BaseServerResponse(serverId, true, linkList);
        BaseServerResponse response2 = new BaseServerResponse(serverId2, true, linkList);
        responseList.add(response);
        responseList.add(response2);

        return responseList;
    }
}

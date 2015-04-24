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
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.IdServerRef;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.ServerRef;
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
    private final static Integer snapshotExpirationDays = 1;

    private static Link link;

    private IdServerRef serverRef;
    private ServerMetadata serverMetadata;

    /* mocked server client*/
    ServerClient serverClient;

    /* mocked queue client*/
    QueueClient queueClient;

    public ServerRef getServerRef() {
        return serverRef;
    }

    public ServerMetadata getServerMetadata() {
        return serverMetadata;
    }

    public ServerStubFixture(ServerClient serverClient, QueueClient queueClient) {
        this.serverClient = serverClient;
        this.queueClient = queueClient;

        initMockAndStubs();
    }

    private void initMockAndStubs() {
        serverMetadata = createServerMetadata();
        serverRef = serverMetadata.asRefById();
        link = createLink();

        GetStatusResponse statusResponse = new GetStatusResponse("succeeded");
        when(queueClient.getJobStatus(anyString())).thenReturn(statusResponse);

        List<String> serverIdList = new ArrayList<>();
        serverIdList.add(serverMetadata.getId());

        BaseServerListResponse baseServerListResponse = createBaseServerListResponse();

        when(serverClient.findServerById(serverIdList.get(0)))
            .thenReturn(serverMetadata);

        when(serverClient.powerOn(anyListOf(String.class)))
            .thenAnswer(
                new Answer<BaseServerListResponse>() {
                    public BaseServerListResponse answer(InvocationOnMock invocation) {
                        serverMetadata.getDetails().setPowerState("started");
                        return baseServerListResponse;
                    }
                }
            );

        when(serverClient.powerOff(anyListOf(String.class)))
            .thenAnswer(
                new Answer<BaseServerListResponse>() {
                    public BaseServerListResponse answer(InvocationOnMock invocation) {
                        serverMetadata.getDetails().setPowerState("stopped");
                        return baseServerListResponse;
                    }
                }
            );

        when(serverClient.startMaintenance(anyListOf(String.class)))
            .thenAnswer(
                new Answer<BaseServerListResponse>() {
                    public BaseServerListResponse answer(InvocationOnMock invocation) {
                        serverMetadata.getDetails().setInMaintenanceMode(true);
                        return baseServerListResponse;
                    }
                }
            );

        when(serverClient.stopMaintenance(anyListOf(String.class)))
            .thenAnswer(
                new Answer<BaseServerListResponse>() {
                    public BaseServerListResponse answer(InvocationOnMock invocation) {
                        serverMetadata.getDetails().setInMaintenanceMode(false);
                        return baseServerListResponse;
                    }
                }
            );

        when(serverClient.pause(anyListOf(String.class)))
            .thenAnswer(
                new Answer<BaseServerListResponse>() {
                    public BaseServerListResponse answer(InvocationOnMock invocation) {
                        serverMetadata.getDetails().setPowerState("paused");
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
                        return baseServerListResponse;
                    }
                }
            );

        when(serverClient.restore(anyString(), any(RestoreServerRequest.class)))
            .thenAnswer(
                new Answer<Link>() {
                    public Link answer(InvocationOnMock invocation) {
                        serverMetadata.setStatus("active");
                        return link;
                    }
                }
            );

        when(serverClient.shutDown(anyListOf(String.class)))
            .thenAnswer(
                new Answer<BaseServerListResponse>() {
                    public BaseServerListResponse answer(InvocationOnMock invocation) {
                        serverMetadata.getDetails().setPowerState("stopped");
                        return baseServerListResponse;
                    }
                }
            );

        when(serverClient.createSnapshot(any(CreateSnapshotRequest.class)))
            .thenAnswer(
                new Answer<BaseServerListResponse>() {
                    public BaseServerListResponse answer(InvocationOnMock invocation) {
                        serverMetadata.getDetails().getSnapshots().add(new Object());
                        return baseServerListResponse;
                    }
                }
            );
    }

    public ServerMetadata createServerMetadata() {
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
        responseList.add(response);

        return responseList;
    }
}

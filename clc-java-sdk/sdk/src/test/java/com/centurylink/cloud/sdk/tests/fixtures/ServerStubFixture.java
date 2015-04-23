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
import com.google.inject.Inject;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Server stub with serverService mocked methods
 */
@Test
public class ServerStubFixture {

    private final static String serverId = "de1altdtcrt777";
    private final static Integer snapshotExpirationDays = 1;

    private static Link link;

    private static volatile IdServerRef serverRef;
    private static volatile ServerMetadata serverMetadata;

    @Inject
    ServerClient serverClient;

    @Inject
    QueueClient queueClient;

    public static ServerRef getServerRef() {
        return serverRef;
    }

    public static ServerMetadata getServerMetadata() {
        return serverMetadata;
    }

    @BeforeSuite(groups = LONG_RUNNING, enabled = false)
    public void initMockAndStubs() {
        serverMetadata = createServerMetadata();
        serverRef = serverMetadata.asRefById();
        link = createLink();

        GetStatusResponse statusResponse = new GetStatusResponse("succeeded");
        when(queueClient.getJobStatus(anyString())).thenReturn(statusResponse);

        List<String> serverIdList = new ArrayList<>();
        serverIdList.add(serverMetadata.getId());

        BaseServerListResponse baseServerListResponse = createBaseServerListResponse();
        CreateSnapshotRequest createSnapshotRequest = createSnapshotRequest(serverIdList);
        RestoreServerRequest restoreServerRequest = createRestoreServerRequest();

        when(serverClient.findServerById(serverIdList.get(0))).thenReturn(serverMetadata);
        when(serverClient.powerOn(serverIdList)).thenReturn(baseServerListResponse);
        when(serverClient.powerOff(serverIdList)).thenReturn(baseServerListResponse);
        when(serverClient.startMaintenance(serverIdList)).thenReturn(baseServerListResponse);
        when(serverClient.stopMaintenance(serverIdList)).thenReturn(baseServerListResponse);
        when(serverClient.pause(serverIdList)).thenReturn(baseServerListResponse);
        when(serverClient.reset(serverIdList)).thenReturn(baseServerListResponse);
        when(serverClient.reboot(serverIdList)).thenReturn(baseServerListResponse);
        when(serverClient.archive(serverIdList)).thenReturn(baseServerListResponse);
        when(serverClient.restore(serverIdList.get(0), restoreServerRequest)).thenReturn(link);
        when(serverClient.shutDown(serverIdList)).thenReturn(baseServerListResponse);
        when(serverClient.createSnapshot(createSnapshotRequest)).thenReturn(baseServerListResponse);
    }

    private ServerMetadata createServerMetadata() {
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

    private RestoreServerRequest createRestoreServerRequest() {
        return new RestoreServerRequest()
                .targetGroupId(serverMetadata.getGroupId());
    }

    private CreateSnapshotRequest createSnapshotRequest(List<String> serverIdList) {
        CreateSnapshotRequest request = new CreateSnapshotRequest();

        request.setSnapshotExpirationDays(snapshotExpirationDays);
        request.setServerIds(serverIdList);

        return request;
    }
}

package com.centurylink.cloud.sdk.tests.fixtures;

import com.centurylink.cloud.sdk.core.client.domain.Link;
import com.centurylink.cloud.sdk.core.commons.client.DataCentersClient;
import com.centurylink.cloud.sdk.core.commons.client.QueueClient;
import com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.GetDataCenterListResponse;
import com.centurylink.cloud.sdk.core.commons.client.domain.queue.GetStatusResponse;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.ChangeInfo;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.server.*;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * Server stub with serverService mocked methods
 */
public class ServerStubFixture {

    private final static String serverId = "de1altdtcrt777";
    private final static String serverId2 = "de1altdtcrt888";
    private final static String groupId = "e9cf5a7a9fad43a8a9184d0265ae076c";

    private static Link link;

    private ServerMetadata serverMetadata1;
    private ServerMetadata serverMetadata2;

    private GroupMetadata groupMetadata;

    private DataCenterMetadata dataCenterMetadata;

    /* mocked server client*/
    ServerClient serverClient;

    /* mocked queue client*/
    QueueClient queueClient;

    /* mocked dataCenter client*/
    DataCentersClient dataCentersClient;

    public ServerStubFixture(ServerClient serverClient, QueueClient queueClient, DataCentersClient dataCentersClient) {
        this.serverClient = serverClient;
        this.queueClient = queueClient;
        this.dataCentersClient = dataCentersClient;

        initMockAndStubs();
    }

    private void initMockAndStubs() {
        serverMetadata1 = createServerMetadata(serverId);
        serverMetadata2 = createServerMetadata(serverId2);

        groupMetadata = createGroupMetadata();
        dataCenterMetadata = createDataCenterMetadata();
        link = createLink();

        GetStatusResponse statusResponse = new GetStatusResponse("succeeded");
        when(queueClient.getJobStatus(anyString())).thenReturn(statusResponse);

        List<String> serverIdList = new ArrayList<>();
        serverIdList.add(serverMetadata1.getId());
        serverIdList.add(serverMetadata2.getId());

        BaseServerListResponse baseServerListResponse = createBaseServerListResponse();

        when(serverClient.findServerById(serverIdList.get(0)))
            .thenReturn(serverMetadata1);

        when(serverClient.findServerById(serverIdList.get(1)))
                .thenReturn(serverMetadata1);

        GetDataCenterListResponse dataCenterListResponse = createDataCenterListResponse();

        when(dataCentersClient.findAllDataCenters())
                .thenReturn(dataCenterListResponse);

        when(serverClient.getGroup(eq(groupId), any(Boolean.class)))
                .thenReturn(groupMetadata);

        when(serverClient.powerOn(anyListOf(String.class)))
            .thenAnswer(
                new Answer<BaseServerListResponse>() {
                    public BaseServerListResponse answer(InvocationOnMock invocation) {
                        serverMetadata1.getDetails().setPowerState("started");
                        serverMetadata2.getDetails().setPowerState("started");
                        return baseServerListResponse;
                    }
                }
            );

        when(serverClient.powerOff(anyListOf(String.class)))
            .thenAnswer(
                new Answer<BaseServerListResponse>() {
                    public BaseServerListResponse answer(InvocationOnMock invocation) {
                        serverMetadata1.getDetails().setPowerState("stopped");
                        serverMetadata2.getDetails().setPowerState("stopped");
                        return baseServerListResponse;
                    }
                }
            );

        when(serverClient.startMaintenance(anyListOf(String.class)))
            .thenAnswer(
                new Answer<BaseServerListResponse>() {
                    public BaseServerListResponse answer(InvocationOnMock invocation) {
                        serverMetadata1.getDetails().setInMaintenanceMode(true);
                        serverMetadata2.getDetails().setInMaintenanceMode(true);
                        return baseServerListResponse;
                    }
                }
            );

        when(serverClient.stopMaintenance(anyListOf(String.class)))
            .thenAnswer(
                new Answer<BaseServerListResponse>() {
                    public BaseServerListResponse answer(InvocationOnMock invocation) {
                        serverMetadata1.getDetails().setInMaintenanceMode(false);
                        serverMetadata2.getDetails().setInMaintenanceMode(false);
                        return baseServerListResponse;
                    }
                }
            );

        when(serverClient.pause(anyListOf(String.class)))
            .thenAnswer(
                new Answer<BaseServerListResponse>() {
                    public BaseServerListResponse answer(InvocationOnMock invocation) {
                        serverMetadata1.getDetails().setPowerState("paused");
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
                        serverMetadata1.setStatus("archived");
                        serverMetadata2.setStatus("archived");
                        return baseServerListResponse;
                    }
                }
            );

        when(serverClient.restore(anyString(), any(RestoreServerRequest.class)))
            .thenAnswer(
                new Answer<Link>() {
                    public Link answer(InvocationOnMock invocation) {
                        serverMetadata1.setStatus("active");
                        serverMetadata2.setStatus("active");
                        return link;
                    }
                }
            );

        when(serverClient.shutDown(anyListOf(String.class)))
            .thenAnswer(
                new Answer<BaseServerListResponse>() {
                    public BaseServerListResponse answer(InvocationOnMock invocation) {
                        serverMetadata1.getDetails().setPowerState("stopped");
                        serverMetadata2.getDetails().setPowerState("stopped");
                        return baseServerListResponse;
                    }
                }
            );

        when(serverClient.createSnapshot(any(CreateSnapshotRequest.class)))
            .thenAnswer(
                new Answer<BaseServerListResponse>() {
                    public BaseServerListResponse answer(InvocationOnMock invocation) {
                        serverMetadata1.getDetails().getSnapshots().add(new Snapshot());
                        serverMetadata2.getDetails().getSnapshots().add(new Snapshot());
                        return baseServerListResponse;
                    }
                }
            );
    }

    public ServerMetadata getServerMetadata() {
        return serverMetadata1;
    }

    public ServerMetadata getAnotherServerMetadata() {
        return serverMetadata2;
    }

    public GroupMetadata getGroupMetadata() {
        return groupMetadata;
    }

    public DataCenterMetadata getDataCenterMetadata() {
        return dataCenterMetadata;
    }

    public ServerFilter getServerFilterById() {
        return new ServerFilter().id(serverMetadata1.getId(), serverMetadata2.getId());
    }

    public GroupFilter getGroupFilterById() {
        return new GroupFilter().id(groupId);
    }

    public ServerFilter getServerFilterByGroupId() {
        return new ServerFilter().groupId(groupId);
    }

    /* replace with restore stub */
    public void activateServers() {
        serverMetadata1.setStatus("active");
        serverMetadata2.setStatus("active");
    }

    private ServerMetadata createServerMetadata(String serverId) {
        return
            new ServerMetadata() {{
                setId(serverId);
                setName(serverId.toUpperCase());
                setGroupId(groupId);
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

    private GroupMetadata createGroupMetadata() {
        return new GroupMetadata() {{
            setId(groupId);
            setName("Default Group");
            setLocationId("DE1");
            setType("default");
            setStatus("active");
            setServersCount(3);
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

    private DataCenterMetadata createDataCenterMetadata() {
        Link link = new Link() {{
            setRel("group");
            setHref("/v2/groups/altd/" + groupId);
            setId(groupId);
            setName("DE1 Hardware");
        }};

        List<Link> links = new ArrayList<>();
        links.add(link);

        return new DataCenterMetadata() {{
            setId("de1");
            setName("DE1 - Germany (Frankfurt)");
            setLinks(links);
        }};
    }

    private BaseServerListResponse createBaseServerListResponse() {
        BaseServerListResponse responseList = new BaseServerListResponse();

        List<Link> linkList = new ArrayList<>();
        linkList.add(link);

        BaseServerResponse response = new BaseServerResponse(serverId, true, linkList, null);
        BaseServerResponse response2 = new BaseServerResponse(serverId2, true, linkList, null);
        responseList.add(response);
        responseList.add(response2);

        return responseList;
    }

    private GetDataCenterListResponse createDataCenterListResponse() {
        GetDataCenterListResponse responseList = new GetDataCenterListResponse();
        responseList.add(dataCenterMetadata);

        return responseList;
    }
}

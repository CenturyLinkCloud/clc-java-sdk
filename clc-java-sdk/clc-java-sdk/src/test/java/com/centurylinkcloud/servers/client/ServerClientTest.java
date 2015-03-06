package com.centurylinkcloud.servers.client;

import com.centurylinkcloud.core.auth.config.AuthModule;
import com.centurylinkcloud.core.auth.domain.credentials.StaticCredentialsProvider;
import com.centurylinkcloud.servers.AbstractServersSdkTest;
import com.centurylinkcloud.servers.client.domain.GetDataCenterResponse;
import com.centurylinkcloud.servers.client.ServerClient;
import com.centurylinkcloud.servers.client.domain.datacenter.deployment.capabilities.GetDeploymentCapabilitiesResponse;
import com.centurylinkcloud.servers.client.domain.group.GetGroupResponse;
import com.centurylinkcloud.servers.config.ServersModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import org.junit.Before;
import org.junit.Test;

import static com.centurylinkcloud.servers.domain.datacenter.DataCenters.DE_FRANKFURT;

/**
 * @author ilya.drabenia
 */
public class ServerClientTest extends AbstractServersSdkTest {

    @Inject
    private ServerClient client;

    @Test
    public void getDataCenterTest() {
        GetDataCenterResponse result = client.getDataCenter(DE_FRANKFURT.getId());

        assert result.getId() != null;
    }

    @Test
    public void getGroupsTest() {
        String rootGroupId = client
            .getDataCenter(DE_FRANKFURT.getId())
            .getGroup()
            .getId();

        GetGroupResponse groupResult = client.getGroup(rootGroupId);

        assert groupResult.getId() != null;
        assert groupResult.findGroupByName("Archive") != null;
    }

    @Test
    public void getDeploymentCapabilitiesTest() {
        GetDeploymentCapabilitiesResponse deployment =
                client.getDataCenterDeploymentCapabilities(DE_FRANKFURT.getId());

        assert deployment.getTemplates().size() > 0;
    }

}

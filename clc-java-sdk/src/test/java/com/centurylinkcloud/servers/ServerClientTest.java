package com.centurylinkcloud.servers;

import com.centurylinkcloud.servers.client.domain.GetDataCenterResult;
import com.centurylinkcloud.servers.client.ServerClient;
import com.centurylinkcloud.servers.client.domain.datacenter.deployment.capabilities.GetDeploymentCapabilitiesResult;
import com.centurylinkcloud.servers.client.domain.group.GetGroupResult;
import com.centurylinkcloud.servers.config.ServersModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import org.junit.Before;
import org.junit.Test;

import static com.centurylinkcloud.servers.domain.datacenter.DataCenters.DE_FRANKFURT;

/**
 * @author ilya.drabenia
 */
public class ServerClientTest {

    @Inject
    private ServerClient client;

    @Before
    public void injectDependencies() {
        Guice
            .createInjector(new ServersModule())
            .injectMembers(this);
    }

    @Test
    public void getDataCenterTest() {
        GetDataCenterResult result = client.getDataCenter(DE_FRANKFURT.getId());

        assert result.getId() != null;
    }

    @Test
    public void getGroupsTest() {
        String rootGroupId = client
            .getDataCenter(DE_FRANKFURT.getId())
            .getGroup()
            .getId();

        GetGroupResult groupResult = client.getGroup(rootGroupId);

        assert groupResult.getId() != null;
        assert groupResult.findGroupByName("Archive") != null;
    }

    @Test
    public void getDeploymentCapabilitiesTest() {
        GetDeploymentCapabilitiesResult deployment =
                client.getDataCenterDeploymentCapabilities(DE_FRANKFURT.getId());

        assert deployment.getTemplates().size() > 0;
    }

}

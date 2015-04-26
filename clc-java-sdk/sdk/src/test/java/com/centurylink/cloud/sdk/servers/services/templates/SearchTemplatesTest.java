package com.centurylink.cloud.sdk.servers.services.templates;

import com.centurylink.cloud.sdk.core.commons.client.DataCentersClient;
import com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.GetDataCenterListResponse;
import com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.deployment.capabilities.GetDeploymentCapabilitiesResponse;
import com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.deployment.capabilities.TemplateMetadata;
import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.DataCenters;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.services.TemplateService;
import com.centurylink.cloud.sdk.servers.services.domain.template.Template;
import com.centurylink.cloud.sdk.tests.mocks.Mocks;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.servers.services.domain.os.CpuArchitecture.x86_64;
import static com.centurylink.cloud.sdk.servers.services.domain.os.OsType.RHEL;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Ilya Drabenia
 */
public class SearchTemplatesTest extends AbstractServersSdkTest {

    @Inject
    TemplateService templateService;

    @Mock
    DataCentersClient dataCentersClient;

    @Test
    public void testFindTemplateByRef_success() throws Exception {
        when(dataCentersClient.findAllDataCenters())
        .thenReturn(
            new ObjectMapper().readValue(
                getClass().getResourceAsStream("data_centers_list.json"),
                GetDataCenterListResponse.class
            )
        );

        when(
            dataCentersClient.getDataCenterDeploymentCapabilities("va1")
        )
        .thenReturn(
            new ObjectMapper().readValue(
                getClass().getResourceAsStream("va1_deployment_capabilities.json"),
                GetDeploymentCapabilitiesResponse.class
            )
        );

        TemplateMetadata metadata = templateService.findByRef(Template.refByOs()
            .dataCenter(DataCenters.US_EAST_STERLING)
            .type(RHEL)
            .edition("6")
            .architecture(x86_64)
        );

        assertEquals(metadata.getName(), "RHEL-6-64-TEMPLATE");
    }

}

package com.centurylink.cloud.sdk.servers.services.templates;

import com.centurylink.cloud.sdk.core.commons.client.DataCentersClient;
import com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.GetDataCenterListResponse;
import com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.deployment.capabilities.DatacenterDeploymentCapabilitiesMetadata;
import com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.deployment.capabilities.TemplateMetadata;
import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.DataCenters;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.services.TemplateService;
import com.centurylink.cloud.sdk.servers.services.domain.template.Template;
import com.google.inject.Inject;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.servers.services.domain.os.CpuArchitecture.x86_64;
import static com.centurylink.cloud.sdk.servers.services.domain.os.OsType.RHEL;

/**
 * @author Ilya Drabenia
 */
public class SearchTemplatesTest extends AbstractServersSdkTest {

    @Inject
    TemplateService templateService;

    @Inject @Spy
    DataCentersClient dataCentersClient;

    @BeforeMethod
    public void setUp() throws Exception {
        mockDataCentersList();
        mockVa1();
    }

    private void mockDataCentersList() {
        Mockito
            .doReturn(fromJson("data_centers_list.json", GetDataCenterListResponse.class))
            .when(dataCentersClient).findAllDataCenters();
    }

    private void mockVa1() {
        Mockito
            .doReturn(
                fromJson("va1_deployment_capabilities.json", DatacenterDeploymentCapabilitiesMetadata.class)
            )
            .when(dataCentersClient).getDataCenterDeploymentCapabilities("va1");
    }

    @Test
    public void testFindTemplateByOsRef() throws Exception {
        TemplateMetadata metadata = templateService.findByRef(Template.refByOs()
                .dataCenter(DataCenters.US_EAST_STERLING)
                .type(RHEL)
                .edition("6")
                .architecture(x86_64)
        );

        assertEquals(metadata.getName(), "RHEL-6-64-TEMPLATE");
    }

    @Test
    public void testFindTemplateByNameRef() {
        TemplateMetadata metadata = templateService.findByRef(Template.refByName()
                .dataCenter(DataCenters.US_EAST_STERLING)
                .name("CENTOS-6-64-TEMPLATE")
        );

        assertEquals(metadata.getName(), "CENTOS-6-64-TEMPLATE");
    }

}

package com.centurylink.cloud.sdk.servers.services.templates;

import com.centurylink.cloud.sdk.common.management.client.domain.datacenters.deployment.capabilities.TemplateMetadata;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.services.TemplateService;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter.DE_FRANKFURT;
import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;

@Test(groups = INTEGRATION)
public class TemplateServiceTest extends AbstractServersSdkTest {

    @Inject
    TemplateService templateService;

    @Test
    public void testFindByDataCenter() {
        List<TemplateMetadata> templates = templateService.findByDataCenter(DE_FRANKFURT.getId());

        assert templates.size() > 0;
    }

}
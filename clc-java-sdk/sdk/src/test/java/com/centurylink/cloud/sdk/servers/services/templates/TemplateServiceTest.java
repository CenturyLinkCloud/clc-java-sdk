package com.centurylink.cloud.sdk.servers.services.templates;

import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.services.TemplateService;
import com.centurylink.cloud.sdk.servers.services.domain.template.Template;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.DataCenter.DE_FRANKFURT;

@Test(groups = INTEGRATION)
public class TemplateServiceTest extends AbstractServersSdkTest {

    @Inject
    TemplateService templateService;

    @Test
    public void testFindByDataCenter() {
        List<Template> templates = templateService.findByDataCenter(DE_FRANKFURT.getId());

        assert templates.size() > 0;
    }

}
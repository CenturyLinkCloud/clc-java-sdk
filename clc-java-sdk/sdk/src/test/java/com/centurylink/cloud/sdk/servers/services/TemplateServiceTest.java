package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.services.domain.datacenter.DataCenters;
import com.centurylink.cloud.sdk.servers.services.domain.template.Template;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.servers.services.domain.datacenter.DataCenters.DE_FRANKFURT;

public class TemplateServiceTest extends AbstractServersSdkTest {

    @Inject
    TemplateService templateService;

    @Test
    public void testFindByDataCenter() {
        List<Template> templates = templateService.findByDataCenter(DE_FRANKFURT);

        assert templates.size() > 0;
    }

}
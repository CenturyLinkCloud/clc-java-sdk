package com.centurylink.cloud.sdk.servers.services.domain.template;

import com.centurylink.cloud.sdk.servers.client.domain.datacenter.deployment.capabilities.TemplateResponse;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ilya.drabenia
 */
public class TemplateConverter {

    public List<Template> templateListFrom(List<TemplateResponse> srcTemplates) {
        return new ArrayList<>(Collections2.transform(srcTemplates, toTemplate()));
    }

    private Function<TemplateResponse, Template> toTemplate() {
        return
            new Function<TemplateResponse, Template>() {

                @Override
                public Template apply(TemplateResponse input) {
                    return templateFrom(input);
                }

            };
    }

    public Template templateFrom(TemplateResponse template) {
        return
            new Template()
                .name(template.getName())
                .description(template.getDescription());
    }

}

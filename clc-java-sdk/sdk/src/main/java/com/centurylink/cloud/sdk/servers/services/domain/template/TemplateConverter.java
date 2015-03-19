package com.centurylink.cloud.sdk.servers.services.domain.template;

import com.centurylink.cloud.sdk.core.datacenters.client.domain.deployment.capacilities.TemplateMetadata;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ilya.drabenia
 */
public class TemplateConverter {

    public List<Template> templateListFrom(List<TemplateMetadata> srcTemplates) {
        return new ArrayList<>(Collections2.transform(srcTemplates, toTemplate()));
    }

    private Function<TemplateMetadata, Template> toTemplate() {
        return
            new Function<TemplateMetadata, Template>() {

                @Override
                public Template apply(TemplateMetadata input) {
                    return templateFrom(input);
                }

            };
    }

    public Template templateFrom(TemplateMetadata template) {
        return
            new Template()
                .name(template.getName())
                .description(template.getDescription());
    }

}

/*
 * (c) 2015 CenturyLink Cloud. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.centurylink.cloud.sdk.servers.services.domain.template.refs;

import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.servers.services.domain.template.filters.TemplateFilter;

/**
 * @author ilya.drabenia
 */
public class TemplateByDescriptionRef extends Template {
    private final String description;

    TemplateByDescriptionRef(DataCenter dataCenter, String description) {
        super(dataCenter);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public TemplateByDescriptionRef description(String description) {
        return new TemplateByDescriptionRef(getDataCenter(), description);
    }

    public TemplateByDescriptionRef dataCenter(DataCenter dataCenter) {
        return new TemplateByDescriptionRef(dataCenter, description);
    }

    @Override
    public TemplateFilter asFilter() {
        return (
            new TemplateFilter()
                .dataCenters(getDataCenter())
                .descriptionContains(description)
        );
    }
}

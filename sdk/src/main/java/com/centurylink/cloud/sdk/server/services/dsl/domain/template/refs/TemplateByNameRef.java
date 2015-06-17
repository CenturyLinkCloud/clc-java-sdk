/*
 * (c) 2015 CenturyLink. All Rights Reserved.
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

package com.centurylink.cloud.sdk.server.services.dsl.domain.template.refs;

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.template.filters.TemplateFilter;

/**
 * @author ilya.drabenia
 */
public class TemplateByNameRef extends Template {
    private final String name;

    TemplateByNameRef(DataCenter dataCenter, String name) {
        super(dataCenter);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public TemplateByNameRef name(String name) {
        return new TemplateByNameRef(getDataCenter(), name);
    }

    public TemplateByNameRef dataCenter(DataCenter dataCenter) {
        return new TemplateByNameRef(dataCenter, name);
    }

    @Override
    public TemplateFilter asFilter() {
        return
            new TemplateFilter()
                .dataCenters(getDataCenter())
                .names(name);
    }
}

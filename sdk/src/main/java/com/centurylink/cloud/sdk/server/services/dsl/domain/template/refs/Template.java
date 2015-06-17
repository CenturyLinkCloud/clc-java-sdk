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
import com.centurylink.cloud.sdk.core.services.refs.Reference;
import com.centurylink.cloud.sdk.server.services.dsl.domain.template.filters.TemplateFilter;

/**
 * {@inheritDoc}
 */
public abstract class Template implements Reference<TemplateFilter> {
    private final DataCenter dataCenter;

    Template(DataCenter dataCenter) {
        this.dataCenter = dataCenter;
    }

    /**
     * Method allow refer to required resource by it's ID.
     *
     * @return {@link TemplateByNameRef}
     */
    public static TemplateByNameRef refByName() {
        return new TemplateByNameRef(null, null);
    }

    /**
     * Method allow refer to resolve required template by it's operating system characteristics.
     * All parameters are optional.
     *
     * @return {@link TemplateByOsRef}
     */
    public static TemplateByOsRef refByOs() {
        return new TemplateByOsRef(null, null, null, null, null);
    }

    /**
     * Method allow to refer resource by keyword contains in description of this resource.
     * All parameters are required. If it will be found zero or multiple resource satisfied provided
     * criteria - it will throw exception.
     *
     * @return {@link TemplateByDescriptionRef}
     */
    public static TemplateByDescriptionRef refByDescription() {
        return new TemplateByDescriptionRef(null, null);
    }

    public DataCenter getDataCenter() {
        return dataCenter;
    }

    @Override
    public String toString() {
        return this.toReadableString();
    }
}

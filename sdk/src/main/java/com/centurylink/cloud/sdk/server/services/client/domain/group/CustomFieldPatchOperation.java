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

package com.centurylink.cloud.sdk.server.services.client.domain.group;

import com.centurylink.cloud.sdk.server.services.client.domain.server.CustomField;

import java.util.List;

/**
 * @author Aliaksandr Krasitski
 */
public class CustomFieldPatchOperation extends PatchOperation{
    private List<CustomField> value;

    public CustomFieldPatchOperation(List<CustomField> value) {
        this.value = value;
        setMember(CUSTOM_FIELD);
    }

    public List<CustomField> getValue() {
        return value;
    }

    public void setValue(List<CustomField> value) {
        this.value = value;
    }

    public CustomFieldPatchOperation value(List<CustomField> value) {
        setValue(value);
        return this;
    }
}

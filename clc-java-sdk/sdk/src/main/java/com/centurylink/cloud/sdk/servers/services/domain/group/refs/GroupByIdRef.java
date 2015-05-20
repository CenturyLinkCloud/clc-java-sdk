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

package com.centurylink.cloud.sdk.servers.services.domain.group.refs;

import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Reference that allow to find unique server group by it's ID and owned data center reference
 *
 * @author ilya.drabenia
 */
public class GroupByIdRef extends Group {
    private final String id;

    GroupByIdRef(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public GroupFilter asFilter() {
        checkNotNull(id, "Group ID must be not null");

        return new GroupFilter().id(id);
    }
}

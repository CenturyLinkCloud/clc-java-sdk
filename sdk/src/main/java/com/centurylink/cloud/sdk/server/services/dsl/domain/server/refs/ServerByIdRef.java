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

package com.centurylink.cloud.sdk.server.services.dsl.domain.server.refs;

import com.centurylink.cloud.sdk.server.services.dsl.domain.server.filters.ServerFilter;

import java.util.Objects;

/**
 * @author ilya.drabenia
 */
public class ServerByIdRef extends Server {
    private final String id;

    ServerByIdRef(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public ServerFilter asFilter() {
        return new ServerFilter().id(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServerByIdRef that = (ServerByIdRef) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

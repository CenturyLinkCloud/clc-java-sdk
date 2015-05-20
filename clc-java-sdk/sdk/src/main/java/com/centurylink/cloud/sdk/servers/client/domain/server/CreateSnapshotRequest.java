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

package com.centurylink.cloud.sdk.servers.client.domain.server;

import java.util.List;

public class CreateSnapshotRequest {

    private Integer snapshotExpirationDays;
    private List<String> serverIds;

    private final Integer defaultExpiration = 10;

    public Integer getSnapshotExpirationDays() {
        return snapshotExpirationDays;
    }

    public void setSnapshotExpirationDays(Integer snapshotExpirationDays) {
        if (snapshotExpirationDays == null || snapshotExpirationDays <= 0 || snapshotExpirationDays > defaultExpiration) {
            snapshotExpirationDays = defaultExpiration;
        }

        this.snapshotExpirationDays = snapshotExpirationDays;
    }

    public CreateSnapshotRequest snapshotExpirationDays(Integer snapshotExpirationDays) {
        setSnapshotExpirationDays(snapshotExpirationDays);
        return this;
    }

    public List<String> getServerIds() {
        return serverIds;
    }

    public void setServerIds(List<String> serverIds) {
        this.serverIds = serverIds;
    }

    public CreateSnapshotRequest serverIds(List<String> serverIds) {
        setServerIds(serverIds);
        return this;
    }
}
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

package com.centurylink.cloud.sdk.common.management.services.domain.queue.job;

import com.centurylink.cloud.sdk.core.ToStringMixin;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Ilya Drabenia
 */
public class JobInfo implements ToStringMixin {
    private final String statusId;

    public JobInfo(String statusId) {
        this.statusId = checkNotNull(statusId, "Status ID must be not a null");
    }

    public String getStatusId() {
        return statusId;
    }

    @Override
    public String toString() {
        return this.toReadableString();
    }
}

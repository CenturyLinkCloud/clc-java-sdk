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

import com.centurylink.cloud.sdk.core.function.Predicates;
import com.centurylink.cloud.sdk.core.preconditions.ArgumentPreconditions;
import com.centurylink.cloud.sdk.core.services.refs.Reference;

import static com.centurylink.cloud.sdk.core.preconditions.ArgumentPreconditions.notNull;


/**
 * @author Ilya Drabenia
 */
public class ResourceJobInfo extends JobInfo {
    private final String operation;
    private final Reference target;

    public ResourceJobInfo(String statusId, Reference target) {
        this(statusId, null, target);
    }

    public ResourceJobInfo(String statusId, String operation, Reference target) {
        super(statusId);
        this.target = notNull(target, "Job target must be not null");
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }

    public Reference getTarget() {
        return target;
    }
}

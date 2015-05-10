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

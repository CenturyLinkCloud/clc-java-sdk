package com.centurylink.cloud.sdk.servers.client.domain.group;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aliaksandr.krasitski
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateGroupRequest {
    private List<PatchOperation> operations;

    public List<PatchOperation> getOperations() {
        return operations;
    }

    public UpdateGroupRequest() {
    }

    public UpdateGroupRequest(List<PatchOperation> operations) {
        this.operations = operations;
    }

    public UpdateGroupRequest add(PatchOperation operation) {
        if (this.operations == null) {
            this.operations = new ArrayList<>();
        }
        this.operations.add(operation);
        return this;
    }
}

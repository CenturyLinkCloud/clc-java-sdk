package com.centurylink.cloud.sdk.servers.client.domain.group;

import com.centurylink.cloud.sdk.commons.client.domain.CustomField;

import java.util.List;

/**
 * @author Aliaksandr Krasitski
 */
public class CustomFieldPatchOperation extends PatchOperation{
    private List<CustomField> value;

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

    public CustomFieldPatchOperation(List<CustomField> value) {
        this.value = value;
        setMember(PatchOperation.CUSTOM_FIELD);
    }
}

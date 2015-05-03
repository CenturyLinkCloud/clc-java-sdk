package com.centurylink.cloud.sdk.common.services.client.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author aliaksandr.krasitski
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomField {
    private String id;
    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CustomField id(String id) {
        setId(id);
        return this;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public CustomField value(String value) {
        setValue(value);
        return this;
    }
}

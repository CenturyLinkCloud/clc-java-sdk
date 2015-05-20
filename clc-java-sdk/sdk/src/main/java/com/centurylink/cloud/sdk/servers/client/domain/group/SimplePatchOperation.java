package com.centurylink.cloud.sdk.servers.client.domain.group;

/**
 * @author Aliaksandr Krasitski
 */
public class SimplePatchOperation extends PatchOperation {
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public SimplePatchOperation value(String value) {
        setValue(value);
        return this;
    }

    public SimplePatchOperation member(String member) {
        setMember(member);
        return this;
    }

    public SimplePatchOperation(String member, String value) {
        this.value = value;
        setMember(member);
    }
}

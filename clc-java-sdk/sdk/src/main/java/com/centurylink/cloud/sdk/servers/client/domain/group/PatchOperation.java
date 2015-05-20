package com.centurylink.cloud.sdk.servers.client.domain.group;

/**
 * @author Aliaksandr Krasitski
 */
public class PatchOperation {
    private String op = "set";
    private String member;

    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String CUSTOM_FIELD = "customFields";
    public static final String GROUP_PARENT_ID = "parentGroupId";

    public String getOp() {
        return op;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }
}

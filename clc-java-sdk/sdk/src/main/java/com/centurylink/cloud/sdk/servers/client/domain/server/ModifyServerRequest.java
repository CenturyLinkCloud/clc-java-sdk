package com.centurylink.cloud.sdk.servers.client.domain.server;

public class ModifyServerRequest<T> {

    private String op = "set";
    private String member;
    private T value;

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public ModifyServerRequest<T> op(String op) {
        setOp(op);
        return this;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public ModifyServerRequest<T> member(String member) {
        setMember(member);
        return this;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public ModifyServerRequest<T> value(T value) {
        setValue(value);
        return this;
    }
}

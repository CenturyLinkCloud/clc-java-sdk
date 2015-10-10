package com.centurylink.cloud.sdk.autoscalepolicy.services.dsl.domain;

public class AutoscalePolicyScaleDownWindow {

    private String start;
    private String end;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public AutoscalePolicyScaleDownWindow start(String start) {
        setStart(start);
        return this;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public AutoscalePolicyScaleDownWindow end(String end) {
        setEnd(end);
        return this;
    }
}

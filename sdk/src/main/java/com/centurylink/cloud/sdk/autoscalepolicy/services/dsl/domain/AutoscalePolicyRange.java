package com.centurylink.cloud.sdk.autoscalepolicy.services.dsl.domain;

public class AutoscalePolicyRange {

    private Integer min;
    private Integer max;

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public AutoscalePolicyRange min(Integer min) {
        setMin(min);
        return this;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public AutoscalePolicyRange max(Integer max) {
        setMax(max);
        return this;
    }
}

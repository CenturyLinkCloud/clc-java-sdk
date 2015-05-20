package com.centurylink.cloud.sdk.servers.services.domain.group;

import java.util.ArrayList;
import java.util.List;

public class BillingStats {

    private String date;
    private List<GroupBilling> groups = new ArrayList<>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BillingStats date(String date) {
        setDate(date);
        return this;
    }

    public List<GroupBilling> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupBilling> groups) {
        this.groups = groups;
    }

    public BillingStats group (GroupBilling group) {
        this.groups.add(group);
        return this;
    }

    public BillingStats groups (List<GroupBilling> groups) {
        setGroups(groups);
        return this;
    }
}
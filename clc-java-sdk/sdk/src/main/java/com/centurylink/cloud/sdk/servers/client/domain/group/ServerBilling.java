package com.centurylink.cloud.sdk.servers.client.domain.group;

public class ServerBilling {

    private String templateCost;
    private String archiveCost;
    private String monthlyEstimate;
    private String monthToDate;
    private String currentHour;

    public String getTemplateCost() {
        return templateCost;
    }

    public String getArchiveCost() {
        return archiveCost;
    }

    public String getMonthlyEstimate() {
        return monthlyEstimate;
    }

    public String getMonthToDate() {
        return monthToDate;
    }

    public String getCurrentHour() {
        return currentHour;
    }
}

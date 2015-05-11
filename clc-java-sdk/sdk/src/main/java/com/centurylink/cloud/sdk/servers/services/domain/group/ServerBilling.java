package com.centurylink.cloud.sdk.servers.services.domain.group;

public class ServerBilling {

    private String serverId;
    private String templateCost;
    private String archiveCost;
    private String monthlyEstimate;
    private String monthToDate;
    private String currentHour;

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public ServerBilling serverId(String serverId) {
        setServerId(serverId);
        return this;
    }

    public String getTemplateCost() {
        return templateCost;
    }

    public void setTemplateCost(String templateCost) {
        this.templateCost = templateCost;
    }

    public ServerBilling templateCost(String templateCost) {
        setTemplateCost(templateCost);
        return this;
    }

    public String getArchiveCost() {
        return archiveCost;
    }

    public void setArchiveCost(String archiveCost) {
        this.archiveCost = archiveCost;
    }

    public ServerBilling archiveCost(String archiveCost) {
        setArchiveCost(archiveCost);
        return this;
    }

    public String getMonthlyEstimate() {
        return monthlyEstimate;
    }

    public void setMonthlyEstimate(String monthlyEstimate) {
        this.monthlyEstimate = monthlyEstimate;
    }

    public ServerBilling monthlyEstimate(String monthlyEstimate) {
        setMonthlyEstimate(monthlyEstimate);
        return this;
    }

    public String getMonthToDate() {
        return monthToDate;
    }

    public void setMonthToDate(String monthToDate) {
        this.monthToDate = monthToDate;
    }

    public ServerBilling monthToDate(String monthToDate) {
        setMonthToDate(monthToDate);
        return this;
    }

    public String getCurrentHour() {
        return currentHour;
    }

    public void setCurrentHour(String currentHour) {
        this.currentHour = currentHour;
    }

    public ServerBilling currentHour(String currentHour) {
        setCurrentHour(currentHour);
        return this;
    }
}

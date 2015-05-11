package com.centurylink.cloud.sdk.servers.services.domain.group;

import java.math.BigDecimal;

public class ServerBilling {

    private String serverId;
    private BigDecimal templateCost;
    private BigDecimal archiveCost;
    private BigDecimal monthlyEstimate;
    private BigDecimal monthToDate;
    private BigDecimal currentHour;

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

    public BigDecimal getTemplateCost() {
        return templateCost;
    }

    public void setTemplateCost(BigDecimal templateCost) {
        this.templateCost = templateCost;
    }

    public ServerBilling templateCost(BigDecimal templateCost) {
        setTemplateCost(templateCost);
        return this;
    }

    public BigDecimal getArchiveCost() {
        return archiveCost;
    }

    public void setArchiveCost(BigDecimal archiveCost) {
        this.archiveCost = archiveCost;
    }

    public ServerBilling archiveCost(BigDecimal archiveCost) {
        setArchiveCost(archiveCost);
        return this;
    }

    public BigDecimal getMonthlyEstimate() {
        return monthlyEstimate;
    }

    public void setMonthlyEstimate(BigDecimal monthlyEstimate) {
        this.monthlyEstimate = monthlyEstimate;
    }

    public ServerBilling monthlyEstimate(BigDecimal monthlyEstimate) {
        setMonthlyEstimate(monthlyEstimate);
        return this;
    }

    public BigDecimal getMonthToDate() {
        return monthToDate;
    }

    public void setMonthToDate(BigDecimal monthToDate) {
        this.monthToDate = monthToDate;
    }

    public ServerBilling monthToDate(BigDecimal monthToDate) {
        setMonthToDate(monthToDate);
        return this;
    }

    public BigDecimal getCurrentHour() {
        return currentHour;
    }

    public void setCurrentHour(BigDecimal currentHour) {
        this.currentHour = currentHour;
    }

    public ServerBilling currentHour(BigDecimal currentHour) {
        setCurrentHour(currentHour);
        return this;
    }
}

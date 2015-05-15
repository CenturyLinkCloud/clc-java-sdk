package com.centurylink.cloud.sdk.servers.services.domain.statistics;

import java.math.BigDecimal;

public class Statistics {

    private BigDecimal templateCost = BigDecimal.ZERO;
    private BigDecimal archiveCost = BigDecimal.ZERO;
    private BigDecimal monthlyEstimate = BigDecimal.ZERO;
    private BigDecimal monthToDate = BigDecimal.ZERO;
    private BigDecimal currentHour = BigDecimal.ZERO;

    public BigDecimal getTemplateCost() {
        return templateCost;
    }

    public void setTemplateCost(BigDecimal templateCost) {
        this.templateCost = templateCost;
    }

    public Statistics templateCost(BigDecimal templateCost) {
        setTemplateCost(templateCost);
        return this;
    }

    public BigDecimal getArchiveCost() {
        return archiveCost;
    }

    public void setArchiveCost(BigDecimal archiveCost) {
        this.archiveCost = archiveCost;
    }

    public Statistics archiveCost(BigDecimal archiveCost) {
        setArchiveCost(archiveCost);
        return this;
    }

    public BigDecimal getMonthlyEstimate() {
        return monthlyEstimate;
    }

    public void setMonthlyEstimate(BigDecimal monthlyEstimate) {
        this.monthlyEstimate = monthlyEstimate;
    }

    public Statistics monthlyEstimate(BigDecimal monthlyEstimate) {
        setMonthlyEstimate(monthlyEstimate);
        return this;
    }

    public BigDecimal getMonthToDate() {
        return monthToDate;
    }

    public void setMonthToDate(BigDecimal monthToDate) {
        this.monthToDate = monthToDate;
    }

    public Statistics monthToDate(BigDecimal monthToDate) {
        setMonthToDate(monthToDate);
        return this;
    }

    public BigDecimal getCurrentHour() {
        return currentHour;
    }

    public void setCurrentHour(BigDecimal currentHour) {
        this.currentHour = currentHour;
    }

    public Statistics currentHour(BigDecimal currentHour) {
        setCurrentHour(currentHour);
        return this;
    }
}
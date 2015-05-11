package com.centurylink.cloud.sdk.servers.client.domain.group;

import java.math.BigDecimal;

public class ClientServerBilling {

    private BigDecimal templateCost;
    private BigDecimal archiveCost;
    private BigDecimal monthlyEstimate;
    private BigDecimal monthToDate;
    private BigDecimal currentHour;

    public BigDecimal getTemplateCost() {
        return templateCost;
    }

    public BigDecimal getArchiveCost() {
        return archiveCost;
    }

    public BigDecimal getMonthlyEstimate() {
        return monthlyEstimate;
    }

    public BigDecimal getMonthToDate() {
        return monthToDate;
    }

    public BigDecimal getCurrentHour() {
        return currentHour;
    }
}

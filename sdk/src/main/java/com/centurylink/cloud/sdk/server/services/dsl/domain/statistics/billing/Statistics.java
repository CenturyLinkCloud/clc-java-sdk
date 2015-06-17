/*
 * (c) 2015 CenturyLink. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.centurylink.cloud.sdk.server.services.dsl.domain.statistics.billing;

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
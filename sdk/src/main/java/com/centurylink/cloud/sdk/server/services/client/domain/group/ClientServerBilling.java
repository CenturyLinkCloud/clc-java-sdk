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

package com.centurylink.cloud.sdk.server.services.client.domain.group;

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

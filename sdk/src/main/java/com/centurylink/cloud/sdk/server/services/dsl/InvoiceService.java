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

package com.centurylink.cloud.sdk.server.services.dsl;

import com.centurylink.cloud.sdk.server.services.client.ServerClient;
import com.centurylink.cloud.sdk.server.services.dsl.domain.invoice.InvoiceData;
import com.google.inject.Inject;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Aliaksandr Krasitski
 */
public class InvoiceService {

    private final ServerClient client;

    @Inject
    public InvoiceService(ServerClient client) {
        this.client = client;
    }

    /**
     * Gets a list of invoicing data for a given account alias for a given month.
     * @param year Year of usage
     * @param month Monthly period of usage
     * @param pricingAccountAlias Short code of the account that sends the invoice for the accountAlias
     * @return the invoice data
     */
    public InvoiceData getInvoice(int year, int month, String pricingAccountAlias) {
        return client.getInvoice(year, month, pricingAccountAlias);
    }

    /**
     * Gets a list of invoicing data for a given account alias for a given month.
     * @param date Date of usage
     * @param pricingAccountAlias Short code of the account that sends the invoice for the accountAlias
     * @return the invoice data
     */
    public InvoiceData getInvoice(Date date, String pricingAccountAlias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return getInvoice(calendar, pricingAccountAlias);
    }

    /**
     * Gets a list of invoicing data for a given account alias for a given month.
     * @param calendar Date of usage
     * @param pricingAccountAlias Short code of the account that sends the invoice for the accountAlias
     * @return the invoice data
     */
    public InvoiceData getInvoice(Calendar calendar, String pricingAccountAlias) {
        return getInvoice(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, pricingAccountAlias);
    }

    /**
     * Gets a list of invoicing data for a given account alias for a given month.
     * @param date Date of usage
     * @param pricingAccountAlias Short code of the account that sends the invoice for the accountAlias
     * @return the invoice data
     */
    public InvoiceData getInvoice(LocalDate date, String pricingAccountAlias) {
        return getInvoice(date.getYear(), date.getMonthValue(), pricingAccountAlias);
    }

    /**
     * Gets a list of invoicing data for a given account alias for a given month.
     * @param year Year of usage
     * @param month Monthly period of usage
     * @return the invoice data
     */
    public InvoiceData getInvoice(int year, int month) {
        return getInvoice(year, month, null);
    }

    /**
     * Gets a list of invoicing data for a given account alias for a given month.
     * @param date Date of usage
     * @return the invoice data
     */
    public InvoiceData getInvoice(Date date) {
        return getInvoice(date, null);
    }

    /**
     * Gets a list of invoicing data for a given account alias for a given month.
     * @param calendar Date of usage
     * @return the invoice data
     */
    public InvoiceData getInvoice(Calendar calendar) {
        return getInvoice(calendar, null);
    }

    /**
     * Gets a list of invoicing data for a given account alias for a given month.
     * @param date Date of usage
     * @return the invoice data
     */
    public InvoiceData getInvoice(LocalDate date) {
        return getInvoice(date, null);
    }
}

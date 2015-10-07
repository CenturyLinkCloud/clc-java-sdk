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

package com.centurylink.cloud.sdk.server.services.dsl.invoice;

import com.centurylink.cloud.sdk.core.auth.services.BearerAuthentication;
import com.centurylink.cloud.sdk.core.injector.Inject;
import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.dsl.InvoiceService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.invoice.InvoiceData;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Calendar;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;

/**
 * @author Aliaksandr Krasitski
 */
@Test(groups = {RECORDED})
public class InvoiceServiceTest extends AbstractServersSdkTest implements WireMockMixin {

    @Inject
    InvoiceService invoiceService;

    @Inject
    BearerAuthentication authentication;

    private int month = 8;
    private int year = 2015;
    private LocalDate previousMonthDate = LocalDate.now().withMonth(month).withYear(year).minusMonths(1);

    @Test
    public void testInvoiceByParams() {
        InvoiceData data = invoiceService.getInvoice(previousMonthDate.getYear(), previousMonthDate.getMonthValue());

        assert data.getInvoiceDate().getYear() == previousMonthDate.getYear();
        assert data.getInvoiceDate().getMonthValue() == previousMonthDate.getMonthValue() + 1;
    }

    @Test
    public void testInvoiceByDateByAlias() {
        Calendar previousMonthDate = getCalendar();
        previousMonthDate.add(Calendar.MONTH, -1);
        InvoiceData data = invoiceService.getInvoice(previousMonthDate.getTime(), authentication.getAccountAlias());

        assert data.getInvoiceDate().getYear() == previousMonthDate.get(Calendar.YEAR);
        assert data.getInvoiceDate().getMonthValue() == previousMonthDate.get(Calendar.MONTH) + 2;
    }

    @Test
    public void testInvoiceByDate() {
        Calendar previousMonthDate = getCalendar();
        previousMonthDate.add(Calendar.MONTH, -1);
        InvoiceData data = invoiceService.getInvoice(previousMonthDate.getTime());

        assert data.getInvoiceDate().getYear() == previousMonthDate.get(Calendar.YEAR);
        assert data.getInvoiceDate().getMonthValue() == previousMonthDate.get(Calendar.MONTH) + 2;
    }

    private Calendar getCalendar() {
        Calendar previousMonthDate = Calendar.getInstance();
        previousMonthDate.set(Calendar.MONTH, month - 1);
        previousMonthDate.set(Calendar.YEAR, year);

        return previousMonthDate;
    }

    @Test
    public void testInvoiceByLocalDate() {
        InvoiceData data = invoiceService.getInvoice(previousMonthDate);

        assert data.getInvoiceDate().getYear() == previousMonthDate.getYear();
        assert data.getInvoiceDate().getMonthValue() == previousMonthDate.getMonthValue() + 1;
    }
}

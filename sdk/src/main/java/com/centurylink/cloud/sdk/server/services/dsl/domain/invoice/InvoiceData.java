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

package com.centurylink.cloud.sdk.server.services.dsl.domain.invoice;

import com.centurylink.cloud.sdk.core.config.OffsetDateTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author Aliaksandr Krasitski
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvoiceData {
    private String id;
    private String terms;
    private String companyName;
    private String accountAlias;
    private String pricingAccountAlias;
    private String parentAccountAlias;
    private String address1;
    private String city;
    private String stateProvince;
    private String postalCode;
    private String billingContactEmail;
    private String invoiceCCEmail;
    private Long totalAmount;
    @JsonDeserialize(using = OffsetDateTimeDeserializer.class)
    private OffsetDateTime invoiceDate;
    private List<LineItem> lineItems;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAccountAlias() {
        return accountAlias;
    }

    public void setAccountAlias(String accountAlias) {
        this.accountAlias = accountAlias;
    }

    public String getPricingAccountAlias() {
        return pricingAccountAlias;
    }

    public void setPricingAccountAlias(String pricingAccountAlias) {
        this.pricingAccountAlias = pricingAccountAlias;
    }

    public String getParentAccountAlias() {
        return parentAccountAlias;
    }

    public void setParentAccountAlias(String parentAccountAlias) {
        this.parentAccountAlias = parentAccountAlias;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getBillingContactEmail() {
        return billingContactEmail;
    }

    public void setBillingContactEmail(String billingContactEmail) {
        this.billingContactEmail = billingContactEmail;
    }

    public String getInvoiceCCEmail() {
        return invoiceCCEmail;
    }

    public void setInvoiceCCEmail(String invoiceCCEmail) {
        this.invoiceCCEmail = invoiceCCEmail;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public OffsetDateTime getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(OffsetDateTime invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }
}

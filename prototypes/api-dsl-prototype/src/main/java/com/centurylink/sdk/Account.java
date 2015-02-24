package com.centurylink.sdk;

/**
 * @author ilya.drabenia
 */
@Payload
public class Account {
    String ParentAlias;
    String AccountAlias;
    String Location;
    String BusinessName;
    String Address1;
    String Address2;
    String City;
    String StateProvince;
    String PostalCode;
    String Country;
    String Telephone;
    String Fax;
    String TimeZone;
    Boolean ShareParentNetworks;
    String BillingResponsibilityID;

    public String getParentAlias() {

        return ParentAlias;
    }

    public void setParentAlias(String parentAlias) {
        ParentAlias = parentAlias;
    }

    public String getAccountAlias() {
        return AccountAlias;
    }

    public void setAccountAlias(String accountAlias) {
        AccountAlias = accountAlias;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getBusinessName() {
        return BusinessName;
    }

    public void setBusinessName(String businessName) {
        BusinessName = businessName;
    }

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String address1) {
        Address1 = address1;
    }

    public String getAddress2() {
        return Address2;
    }

    public void setAddress2(String address2) {
        Address2 = address2;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getStateProvince() {
        return StateProvince;
    }

    public void setStateProvince(String stateProvince) {
        StateProvince = stateProvince;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public String getFax() {
        return Fax;
    }

    public void setFax(String fax) {
        Fax = fax;
    }

    public String getTimeZone() {
        return TimeZone;
    }

    public void setTimeZone(String timeZone) {
        TimeZone = timeZone;
    }

    public Boolean getShareParentNetworks() {
        return ShareParentNetworks;
    }

    public void setShareParentNetworks(Boolean shareParentNetworks) {
        ShareParentNetworks = shareParentNetworks;
    }

    public String getBillingResponsibilityID() {
        return BillingResponsibilityID;
    }

    public void setBillingResponsibilityID(String billingResponsibilityID) {
        BillingResponsibilityID = billingResponsibilityID;
    }
}

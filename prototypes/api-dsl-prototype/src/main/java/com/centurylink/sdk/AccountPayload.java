package com.centurylink.sdk;

public class AccountPayload {

    public static com.centurylink.sdk.AccountPayload builder() {
        return new com.centurylink.sdk.AccountPayload();
    }


    private java.lang.String TimeZone;

    private java.lang.String Fax;

    private java.lang.String Location;

    private java.lang.String Country;

    private java.lang.String BillingResponsibilityID;

    private java.lang.String ParentAlias;

    private java.lang.String BusinessName;

    private java.lang.String Telephone;

    private java.lang.String StateProvince;

    private java.lang.String AccountAlias;

    private java.lang.String PostalCode;

    private java.lang.String Address2;

    private java.lang.String Address1;

    private java.lang.String City;

    private java.lang.Boolean ShareParentNetworks;



    public com.centurylink.sdk.AccountPayload TimeZone(java.lang.String TimeZone) {
        this.TimeZone = TimeZone;
        return this;
    }

    public com.centurylink.sdk.AccountPayload Fax(java.lang.String Fax) {
        this.Fax = Fax;
        return this;
    }

    public com.centurylink.sdk.AccountPayload Location(java.lang.String Location) {
        this.Location = Location;
        return this;
    }

    public com.centurylink.sdk.AccountPayload Country(java.lang.String Country) {
        this.Country = Country;
        return this;
    }

    public com.centurylink.sdk.AccountPayload BillingResponsibilityID(java.lang.String BillingResponsibilityID) {
        this.BillingResponsibilityID = BillingResponsibilityID;
        return this;
    }

    public com.centurylink.sdk.AccountPayload ParentAlias(java.lang.String ParentAlias) {
        this.ParentAlias = ParentAlias;
        return this;
    }

    public com.centurylink.sdk.AccountPayload BusinessName(java.lang.String BusinessName) {
        this.BusinessName = BusinessName;
        return this;
    }

    public com.centurylink.sdk.AccountPayload Telephone(java.lang.String Telephone) {
        this.Telephone = Telephone;
        return this;
    }

    public com.centurylink.sdk.AccountPayload StateProvince(java.lang.String StateProvince) {
        this.StateProvince = StateProvince;
        return this;
    }

    public com.centurylink.sdk.AccountPayload AccountAlias(java.lang.String AccountAlias) {
        this.AccountAlias = AccountAlias;
        return this;
    }

    public com.centurylink.sdk.AccountPayload PostalCode(java.lang.String PostalCode) {
        this.PostalCode = PostalCode;
        return this;
    }

    public com.centurylink.sdk.AccountPayload Address2(java.lang.String Address2) {
        this.Address2 = Address2;
        return this;
    }

    public com.centurylink.sdk.AccountPayload Address1(java.lang.String Address1) {
        this.Address1 = Address1;
        return this;
    }

    public com.centurylink.sdk.AccountPayload City(java.lang.String City) {
        this.City = City;
        return this;
    }

    public com.centurylink.sdk.AccountPayload ShareParentNetworks(java.lang.Boolean ShareParentNetworks) {
        this.ShareParentNetworks = ShareParentNetworks;
        return this;
    }


    public com.centurylink.sdk.Account build() {
        final com.centurylink.sdk.Account data = new com.centurylink.sdk.Account();


        data.setTimeZone(this.TimeZone);

        data.setFax(this.Fax);

        data.setLocation(this.Location);

        data.setCountry(this.Country);

        data.setBillingResponsibilityID(this.BillingResponsibilityID);

        data.setParentAlias(this.ParentAlias);

        data.setBusinessName(this.BusinessName);

        data.setTelephone(this.Telephone);

        data.setStateProvince(this.StateProvince);

        data.setAccountAlias(this.AccountAlias);

        data.setPostalCode(this.PostalCode);

        data.setAddress2(this.Address2);

        data.setAddress1(this.Address1);

        data.setCity(this.City);

        data.setShareParentNetworks(this.ShareParentNetworks);


        return data;
    }
}
package com.centurylink.cloud.sdk.servers.client.domain.group;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "date",
        "groups"
})
public class ClientBillingStats {

    @JsonProperty("date")
    private String date;
    @JsonProperty("groups")
    private Map<String, ClientGroupBilling> groups = new HashMap<>();


    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("groups")
    public Map<String, ClientGroupBilling> getGroups() {
        return groups;
    }
}
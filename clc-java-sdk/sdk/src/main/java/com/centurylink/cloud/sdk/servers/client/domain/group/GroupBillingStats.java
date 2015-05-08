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
public class GroupBillingStats {

    @JsonProperty("date")
    private String date;
    @JsonProperty("groups")
    private Map<String, GroupBilling> groups = new HashMap<>();


    @JsonProperty("date")
    public String getName() {
        return date;
    }

    @JsonProperty("groups")
    public Map<String, GroupBilling> getGroups() {
        return groups;
    }
}
package com.centurylink.cloud.sdk.server.services.client.domain.server;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Anton Karavayeu
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "userName",
        "password"
})
public class ServerCredentials {

    @JsonProperty(value = "userName", required = true)
    private String userName;
    @JsonProperty(value = "password", required = true)
    private String password;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The userName
     */
    @JsonProperty("userName")
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName The userName
     */
    @JsonProperty("userName")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ServerCredentials userName(String userNameValue) {
        setUserName(userNameValue);
        return this;
    }

    /**
     * @return The password
     */
    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    /**
     * @param password The password
     */
    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }

    public ServerCredentials password(String passwordValue) {
        setPassword(passwordValue);
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

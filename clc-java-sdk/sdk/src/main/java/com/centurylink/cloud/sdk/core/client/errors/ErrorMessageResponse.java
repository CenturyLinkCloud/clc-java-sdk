package com.centurylink.cloud.sdk.core.client.errors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author ilya.drabenia
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorMessageResponse {
    private String message;

    public ErrorMessageResponse(@JsonProperty("message") String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

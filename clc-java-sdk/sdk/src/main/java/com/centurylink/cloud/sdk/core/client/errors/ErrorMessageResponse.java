package com.centurylink.cloud.sdk.core.client.errors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * @author ilya.drabenia
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorMessageResponse {
    private String message;
    private Map<String, List<String>> modelState;

    public ErrorMessageResponse(@JsonProperty("message") String message,
                                @JsonProperty("modelState") Map<String, List<String>> modelState) {
        this.message = message;
        this.modelState = modelState;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, List<String>> getModelState() {
        return modelState;
    }
}

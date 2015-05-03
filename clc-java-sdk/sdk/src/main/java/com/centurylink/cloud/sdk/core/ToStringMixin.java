package com.centurylink.cloud.sdk.core;

import com.centurylink.cloud.sdk.core.exceptions.ClcException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Ilya Drabenia
 */
public interface ToStringMixin {

    default String toReadableString() {
        try {
            return
                new ObjectMapper()
                    .writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            throw new ClcException(ex);
        }
    }

}

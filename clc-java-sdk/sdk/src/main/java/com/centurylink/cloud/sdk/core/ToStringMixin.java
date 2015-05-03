package com.centurylink.cloud.sdk.core;

import com.centurylink.cloud.sdk.core.exceptions.ClcException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping.NON_FINAL;

/**
 * @author Ilya Drabenia
 */
public interface ToStringMixin {

    default String toReadableString() {
        try {
            return
                new ObjectMapper()
                    .enableDefaultTypingAsProperty(NON_FINAL, "class")
                    .writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            throw new ClcException(ex);
        }
    }

}

package com.centurylink.cloud.sdk.servers.services.domain.server;

import com.centurylink.cloud.sdk.core.services.ClcServiceException;

/**
 * @author Ilya Drabenia
 */
public class TimeToLiveParseException extends ClcServiceException {

    public TimeToLiveParseException(String format, Object... arguments) {
        super(format, arguments);
    }

}

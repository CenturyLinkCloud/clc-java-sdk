package com.centurylink.cloud.sdk.core.commons.client;

import com.centurylink.cloud.sdk.core.auth.services.BearerAuthentication;
import com.centurylink.cloud.sdk.core.client.BaseSdkClient;
import com.centurylink.cloud.sdk.core.commons.client.domain.queue.GetStatusResponse;
import com.google.inject.Inject;

/**
 * @author Ilya Drabenia
 */
public class QueueClient extends BaseSdkClient {

    @Inject
    public QueueClient(BearerAuthentication authFilter) {
        super(authFilter);
    }

    public GetStatusResponse getJobStatus(String jobId) {
        return
            client("/operations/{accountAlias}/status/{statusId}")
                .resolveTemplate("statusId", jobId)
                .request()
                .get(GetStatusResponse.class);
    }

}

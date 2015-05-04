package com.centurylink.cloud.sdk.common.management.client;

import com.centurylink.cloud.sdk.common.management.client.domain.queue.GetStatusResponse;
import com.centurylink.cloud.sdk.core.auth.services.BearerAuthentication;
import com.centurylink.cloud.sdk.core.client.SdkHttpClient;
import com.google.inject.Inject;

/**
 * @author Ilya Drabenia
 */
public class QueueClient extends SdkHttpClient {

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

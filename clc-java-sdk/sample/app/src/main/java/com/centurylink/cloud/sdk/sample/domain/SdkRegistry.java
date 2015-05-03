package com.centurylink.cloud.sdk.sample.domain;

import com.centurylink.cloud.sdk.ClcSdk;
import com.centurylink.cloud.sdk.base.auth.services.domain.credentials.StaticCredentialsProvider;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author ilya.drabenia
 */
@Service
public class SdkRegistry {

    private ConcurrentMap<String, ClcSdk> sdkCache = new ConcurrentHashMap<>();

    public SdkRegistry() {
    }

    public ClcSdk findOrCreate(String username, String password) {
        if (sdkCache.get(username) == null) {
            sdkCache.putIfAbsent(username, new ClcSdk(new StaticCredentialsProvider(username, password)));
        }

        return sdkCache.get(username);
    }
}

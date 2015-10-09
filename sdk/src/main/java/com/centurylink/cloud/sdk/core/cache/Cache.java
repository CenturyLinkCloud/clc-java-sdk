/*
 * (c) 2015 CenturyLink. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.centurylink.cloud.sdk.core.cache;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author aliaksandr.krasitski
 */
public class Cache<K, V> {
    private Map<K, V> localCache;

    private long expireAfterWriteMillis = -1;

    private int maxSize = -1;

    private AtomicLong lastUpdated = new AtomicLong(new Date().getTime());

    private CacheLoader<K, V> loader;

    public V get(K key) throws ExecutionException {
        V curValue = localCache.get(key);

        if (!isExpired() && curValue != null) {
            return curValue;
        }

        try {
            V value = loader.load(key);
            updateValue(key, value);
            return value;
        } catch (Exception e) {
            throw new ExecutionException(e);
        }
    }

    private void updateValue(K key, V value) {
        localCache.remove(key);
        localCache.put(key, value);

        lastUpdated.set(new Date().getTime());
    }

    private boolean isExpired() {
        long curTime = new Date().getTime();
        return lastUpdated.get() + expireAfterWriteMillis < curTime;
    }

    public Cache<K, V> expireAfterWrite(long duration, TimeUnit unit) {
        this.expireAfterWriteMillis = unit.toMillis(duration);
        return this;
    }

    public Cache<K, V> build(CacheLoader<K, V> loader) {
        this.localCache = new ConcurrentHashMap<>(maxSize);
        this.loader = loader;
        return this;
    }

    public Cache<K, V> maximumSize(int newSize) {
        this.maxSize = newSize;
        return this;
    }
}

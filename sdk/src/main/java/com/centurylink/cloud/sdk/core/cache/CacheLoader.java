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

/**
 * Computes or retrieves values, based on a key.
 */
public abstract class CacheLoader<K, V> {
    /**
     * Constructor for use by subclasses.
     */
    protected CacheLoader() {}

    /**
     * Computes or retrieves the value corresponding to {@code key}.
     *
     * @param key the non-null key whose value should be loaded
     * @return the value associated with {@code key}; <b>must not be null</b>
     * @throws Exception if unable to load the result
     * @throws InterruptedException if this method is interrupted. {@code InterruptedException} is
     *     treated like any other {@code Exception} in all respects except that, when it is caught,
     *     the thread's interrupt status is set
     */
    public abstract V load(K key) throws Exception;
}


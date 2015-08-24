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

package com.centurylink.cloud.sdk.policy.services.dsl.domain;

import java.time.LocalTime;

/**
 * @author Aliaksandr Krasitski
 */
public class AlertTrigger {
    private AlertTriggerMetric metric;
    private LocalTime duration;
    private Float threshold;

    private static final Float MAX_THRESHOLD = 95f;
    private static final Float MIN_THRESHOLD = 5f;

    public AlertTrigger metric(AlertTriggerMetric metric) {
        this.metric = metric;
        return this;
    }

    public AlertTriggerMetric getMetric() {
        return metric;
    }

    public AlertTrigger duration(LocalTime duration) {
        this.duration = duration;
        return this;
    }

    public AlertTrigger duration(String duration) {
        this.duration = LocalTime.parse(duration);
        return this;
    }

    public AlertTrigger duration(Integer duration) {
        this.duration = LocalTime.of(0, duration, 0);
        return this;
    }

    public LocalTime getDuration() {
        return duration;
    }

    public AlertTrigger threshold(Float threshold) {
        Float newThreshold = new Float(5 * (Math.round(threshold / 5)));

        if (newThreshold < MIN_THRESHOLD) {
            newThreshold = MIN_THRESHOLD;
        }

        if (newThreshold > MAX_THRESHOLD) {
            newThreshold = MAX_THRESHOLD;
        }

        this.threshold = newThreshold;
        return this;
    }

    public Float getThreshold() {
        return threshold;
    }
}

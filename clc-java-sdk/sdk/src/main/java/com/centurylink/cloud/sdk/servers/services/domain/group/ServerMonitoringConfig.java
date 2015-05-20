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

package com.centurylink.cloud.sdk.servers.services.domain.group;

import java.time.Duration;
import java.time.OffsetDateTime;

public class ServerMonitoringConfig {
    private OffsetDateTime from;
    private OffsetDateTime to;
    private Duration interval;
    private MonitoringType type = MonitoringType.HOURLY;

    public static final int MAX_HOURLY_PERIOD_DAYS = 14;
    public static final int MIN_HOURLY_INTERVAL_HOURS = 1;
    public static final Duration DEFAULT_HOURLY_INTERVAL = Duration.ofHours(MIN_HOURLY_INTERVAL_HOURS);

    public static final int MAX_REALTIME_PERIOD_HOURS = 4;
    public static final int MIN_REALTIME_INTERVAL_MINUTES = 5;
    public static final Duration DEFAULT_REALTIME_INTERVAL = Duration.ofMinutes(MIN_REALTIME_INTERVAL_MINUTES);

    public ServerMonitoringConfig last(Duration last) {
        from = OffsetDateTime.now().minus(last);
        return this;
    }

    public OffsetDateTime getFrom() {
        return from;
    }

    public ServerMonitoringConfig from(OffsetDateTime from) {
        this.from = from;
        return this;
    }

    public OffsetDateTime getTo() {
        return to;
    }

    public ServerMonitoringConfig to(OffsetDateTime to) {
        this.to = to;
        return this;
    }

    public Duration getInterval() {
        return interval;
    }

    public ServerMonitoringConfig interval(Duration interval) {
        this.interval = interval;
        return this;
    }

    public MonitoringType getType() {
        return type;
    }

    public ServerMonitoringConfig type(MonitoringType type) {
        this.type = type;
        return this;
    }
}
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

import com.centurylink.cloud.sdk.core.client.ClcClientException;

import java.time.Duration;
import java.time.OffsetDateTime;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author aliaksandr.krasitski
 */
public class ServerMonitoringConfigValidator {
    private ServerMonitoringConfig config;

    private void checkMonitoringConfig(ServerMonitoringConfig config) {
        checkNotNull(config.getFrom(), "From date must be not a null");

        if (config.getInterval() == null) {
            config.interval(
                config.getType() == MonitoringType.HOURLY
                    ? config.DEFAULT_HOURLY_INTERVAL
                    : config.DEFAULT_REALTIME_INTERVAL
            );
        }

        OffsetDateTime to = config.getTo() != null ? config.getTo() : OffsetDateTime.now();

        if (config.getFrom().isAfter(to)) {
            throw new ClcClientException("Start date cannot be more than end date");
        }
        if (Duration.between(config.getFrom(), to).getSeconds() < config.getInterval().getSeconds()) {
            throw new ClcClientException("Interval must fit within start/end date");
        }
    }

    public MonitoringConfigValidator getValidator(ServerMonitoringConfig config) {
        this.config = config;
        switch (config.getType()) {
            case HOURLY: {
                return new HourlyMonitoringConfigValidator();
            }
            case REALTIME: {
                return new RealtimeMonitoringConfigValidator();
            }
            case LATEST: {
                return new LatestMonitoringConfigValidator();
            }
            default: return null;
        }
    }

    class MonitoringConfigValidator {
        public void validate() {

        }
    }

    class HourlyMonitoringConfigValidator extends MonitoringConfigValidator{
        @Override
        public void validate() {
            checkMonitoringConfig(config);

            if (config.getFrom().isBefore(OffsetDateTime.now().minusDays(config.MAX_HOURLY_PERIOD_DAYS))) {
                throw new ClcClientException(String.format(
                    "Start date must be within the past %s day(s)",
                    config.MAX_HOURLY_PERIOD_DAYS)
                );
            }

            if (config.getInterval().toHours() < config.MIN_HOURLY_INTERVAL_HOURS) {
                throw new ClcClientException(String.format(
                    "Interval must be not less than %s hour(s)",
                    config.MIN_HOURLY_INTERVAL_HOURS)
                );
            }
        }
    }

    class RealtimeMonitoringConfigValidator extends MonitoringConfigValidator {
        @Override
        public void validate() {
            checkMonitoringConfig(config);

            if (config.getFrom().isBefore(OffsetDateTime.now().minusHours(config.MAX_REALTIME_PERIOD_HOURS))) {
                throw new ClcClientException(String.format(
                    "Start date must be within the past %s hour(s)",
                    config.MAX_REALTIME_PERIOD_HOURS)
                );
            }

            if (config.getInterval().toMinutes() < config.MIN_REALTIME_INTERVAL_MINUTES) {
                throw new ClcClientException(String.format(
                    "Interval must be not less than %s minute(s)",
                    config.MIN_REALTIME_INTERVAL_MINUTES)
                );
            }
        }
    }

    class LatestMonitoringConfigValidator extends MonitoringConfigValidator {
    }
}

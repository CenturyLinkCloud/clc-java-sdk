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
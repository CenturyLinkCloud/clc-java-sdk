package com.centurylink.cloud.sdk.servers.services.domain.group;

import java.time.Duration;
import java.time.LocalDateTime;

public class ServerMonitoringFilter {
    LocalDateTime from;
    LocalDateTime to = LocalDateTime.now();
    Duration interval;
    MonitoringType type = MonitoringType.HOURLY;

    ServerMonitoringFilter last(Duration last) {
        to = LocalDateTime.now();
        from = to.minus(last);
        return this;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public ServerMonitoringFilter from(LocalDateTime from) {
        this.from = from;
        return this;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public ServerMonitoringFilter to(LocalDateTime to) {
        this.to = to;
        return this;
    }

    public Duration getInterval() {
        return interval;
    }

    public ServerMonitoringFilter interval(Duration interval) {
        this.interval = interval;
        return this;
    }

    public MonitoringType getType() {
        return type;
    }

    public ServerMonitoringFilter type(MonitoringType type) {
        this.type = type;
        return this;
    }
}
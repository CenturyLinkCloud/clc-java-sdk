package com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring;

import java.util.List;

/**
 * @author aliaksandr.krasitski
 */
public class MonitoringStatsEntry <T> {
    private T entity; // ServerMetadata, GroupMetadata, DataCenterMetadata, AccountMetadata
    private List<MonitoringEntry> statistics;

    public MonitoringStatsEntry(T entity, List<MonitoringEntry> statistics) {
        this.entity = entity;
        this.statistics = statistics;
    }

    public T getEntity() {
        return entity;
    }

    public List<MonitoringEntry> getStatistics() {
        return statistics;
    }
}

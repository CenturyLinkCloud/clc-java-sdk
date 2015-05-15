package com.centurylink.cloud.sdk.servers.services.domain.statistics;


public class BillingStatsEntry<T> {

    T entity;
    Statistics statistics;

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public BillingStatsEntry entity(T entity) {
        setEntity(entity);
        return this;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public BillingStatsEntry statistics(Statistics statistics) {
        setStatistics(statistics);
        return this;
    }
}

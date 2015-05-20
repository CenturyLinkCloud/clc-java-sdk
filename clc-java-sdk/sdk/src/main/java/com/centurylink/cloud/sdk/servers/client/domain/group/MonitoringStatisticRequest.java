package com.centurylink.cloud.sdk.servers.client.domain.group;

/**
 * @author aliaksandr.krasitski
 */
public class MonitoringStatisticRequest {
    private String start;
    private String end;
    private String sampleInterval;
    private String type;

    public String getStart() {
        return start;
    }

    public MonitoringStatisticRequest start(String start) {
        this.start = start;
        return this;
    }

    public String getEnd() {
        return end;
    }

    public MonitoringStatisticRequest end(String end) {
        this.end = end;
        return this;
    }

    public String getSampleInterval() {
        return sampleInterval;
    }

    public MonitoringStatisticRequest sampleInterval(String sampleInterval) {
        this.sampleInterval = sampleInterval;
        return this;
    }

    public String getType() {
        return type;
    }

    public MonitoringStatisticRequest type(String type) {
        this.type = type;
        return this;
    }
}

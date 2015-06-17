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

package com.centurylink.cloud.sdk.server.services.client.domain.group;

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

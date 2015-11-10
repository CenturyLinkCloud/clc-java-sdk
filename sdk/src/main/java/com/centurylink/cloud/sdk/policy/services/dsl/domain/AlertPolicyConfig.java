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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Aliaksandr Krasitski
 */
public class AlertPolicyConfig {
    private String name;
    private List<AlertAction> actions = new ArrayList<>();
    private List<AlertTrigger> triggers = new ArrayList<>();

    public AlertPolicyConfig name(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public AlertPolicyConfig actions(List<AlertAction> actions) {
        this.actions.addAll(actions);
        return this;
    }

    public AlertPolicyConfig actions(AlertAction... actions) {
        return actions(Arrays.asList(actions));
    }

    public AlertPolicyConfig action(AlertAction action) {
        this.actions.add(action);
        return this;
    }

    public List<AlertAction> getActions() {
        return actions;
    }

    public AlertPolicyConfig triggers(List<AlertTrigger> triggers) {
        this.triggers.addAll(triggers);
        return this;
    }

    public AlertPolicyConfig triggers(AlertTrigger... triggers) {
        return triggers(Arrays.asList(triggers));
    }

    public AlertPolicyConfig trigger(AlertTrigger trigger) {
        this.triggers.add(trigger);
        return this;
    }

    public List<AlertTrigger> getTriggers() {
        return triggers;
    }
}

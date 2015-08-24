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

import com.centurylink.cloud.sdk.policy.services.client.domain.ActionSettingsEmailMetadata;
import com.centurylink.cloud.sdk.policy.services.client.domain.ActionSettingsMetadata;
import com.centurylink.cloud.sdk.policy.services.client.domain.AlertActionMetadata;
import com.centurylink.cloud.sdk.policy.services.client.domain.AlertPolicyMetadata;
import com.centurylink.cloud.sdk.policy.services.client.domain.AlertPolicyRequest;
import com.centurylink.cloud.sdk.policy.services.client.domain.AlertTriggerMetadata;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author Aliaksandr Krasitski
 */
public class PolicyConverter {

    public AlertPolicyRequest buildCreateAlertPolicyRequest(AlertPolicyConfig config) {
        return new AlertPolicyRequest()
            .name(config.getName())
            .actions(convertActions(config.getActions()))
            .triggers(convertTriggers(config.getTriggers()));
    }

    private List<AlertActionMetadata> convertActions(List<AlertAction> actions) {
        return actions.stream()
            .map(action -> new AlertActionMetadata()
                    .action(action.getAction())
                    .settings(convertSettings(action.getSettings()))
            )
            .collect(toList());
    }

    private List<AlertTriggerMetadata> convertTriggers(List<AlertTrigger> triggers) {
        return triggers.stream()
            .map(trigger -> new AlertTriggerMetadata()
                    .metric(trigger.getMetric().name().toLowerCase())
                    .duration(trigger.getDuration().format(DateTimeFormatter.ISO_LOCAL_TIME))
                    .threshold(trigger.getThreshold())
            )
            .collect(toList());
    }

    private ActionSettingsMetadata convertSettings(ActionSettings settings) {
        if (settings instanceof ActionSettingsEmail) {
            return new ActionSettingsEmailMetadata()
                .recipients(((ActionSettingsEmail) settings).getRecipients());
        }

        return null;
    }

    public AlertPolicyRequest buildModifyAlertPolicyRequest(AlertPolicyConfig modifyConfig,
                    AlertPolicyMetadata policyToUpdate) {

        return new AlertPolicyRequest()
            .name(
                (modifyConfig.getName() == null) ?
                    policyToUpdate.getName() : modifyConfig.getName()
            ).actions(
            (modifyConfig.getActions().size() == 0) ?
                policyToUpdate.getActions() : convertActions(modifyConfig.getActions())
            ).triggers(
                (modifyConfig.getTriggers().size() == 0) ?
                    policyToUpdate.getTriggers() : convertTriggers(modifyConfig.getTriggers())
            );
    }
}

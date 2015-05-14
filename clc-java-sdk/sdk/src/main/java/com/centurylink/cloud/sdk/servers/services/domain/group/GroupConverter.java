package com.centurylink.cloud.sdk.servers.services.domain.group;

import com.centurylink.cloud.sdk.core.client.ClcClientException;
import com.centurylink.cloud.sdk.servers.client.domain.group.*;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author ilya.drabenia
 */
public class GroupConverter {

    public CreateGroupRequest createGroupRequest(GroupConfig groupConfig, String parentGroupId) {
        return new CreateGroupRequest()
            .name(groupConfig.getName())
            .description(groupConfig.getDescription())
            .parentGroupId(parentGroupId)
            .customFields(groupConfig.getCustomFields());
    }

    public UpdateGroupRequest createUpdateGroupRequest(GroupConfig groupConfig, String parentGroupId) {
        UpdateGroupRequest req = new UpdateGroupRequest();

        if (groupConfig.getName() != null) {
            req.add(new SimplePatchOperation(PatchOperation.NAME, groupConfig.getName()));
        }
        if (groupConfig.getDescription() != null) {
            req.add(new SimplePatchOperation(PatchOperation.DESCRIPTION, groupConfig.getDescription()));
        }
        if (parentGroupId != null) {
            req.add(new SimplePatchOperation(PatchOperation.GROUP_PARENT_ID, parentGroupId));
        }
        if (groupConfig.getCustomFields() != null) {
            req.add(new CustomFieldPatchOperation(groupConfig.getCustomFields()));
        }
        return req;
    }

    public GroupConfig createGroupConfig(GroupHierarchyConfig hierarchyConfig, String parentGroupId) {
        return new GroupConfig()
            .name(hierarchyConfig.getName())
            .description(hierarchyConfig.getDescription())
            .parentGroup(Group.refById(parentGroupId));
    }

    public BillingStats convertBillingStats(ClientBillingStats clientBillingStats) {
        List<GroupBilling> groupBillingList = new ArrayList<>();

        clientBillingStats.getGroups().forEach(
            (groupId, clientGroupBilling) -> {
                List<ServerBilling> serverBillingList = new ArrayList<>();

                clientGroupBilling.getServers().forEach(
                    (serverId, clientServerBilling) ->
                        serverBillingList.add(
                            convertServerBilling(serverId, clientServerBilling)
                        )
                );

                groupBillingList.add(
                    convertGroupBilling(groupId, clientGroupBilling, serverBillingList)
                );
            }
        );

        return convertBillingStats(clientBillingStats, groupBillingList);
    }

    private BillingStats convertBillingStats(
            ClientBillingStats clientBillingStats,
            List<GroupBilling> groupBillingList
    ) {
        return
            new BillingStats()
                .date(clientBillingStats.getDate())
                .groups(groupBillingList);
    }

    private GroupBilling convertGroupBilling(
            String groupId,
            ClientGroupBilling clientGroupBilling,
            List<ServerBilling> serverBillingList
    ) {
        return
            new GroupBilling()
                .groupId(groupId)
                .name(clientGroupBilling.getName())
                .servers(serverBillingList);
    }

    private ServerBilling convertServerBilling(String serverId, ClientServerBilling clientServerBilling) {
        return
            new ServerBilling()
                .serverId(serverId)
                .templateCost(clientServerBilling.getTemplateCost())
                .archiveCost(clientServerBilling.getArchiveCost())
                .monthlyEstimate(clientServerBilling.getMonthlyEstimate())
                .monthToDate(clientServerBilling.getMonthToDate())
                .currentHour(clientServerBilling.getCurrentHour());
    }

    public MonitoringStatisticRequest createMonitoringStatisticRequest(ServerMonitoringConfig config) {

        MonitoringStatisticRequest request =
            new MonitoringStatisticRequest()
                .type(config.getType().name());

        switch (config.getType()) {
            case HOURLY: {
                if (config.getInterval() == null) {
                    config.interval(config.DEFAULT_HOURLY_INTERVAL);
                }

                checkMonitoringFilter(config);

                if (config.getFrom().isBefore(OffsetDateTime.now().minusDays(config.MAX_HOURLY_PERIOD_DAYS))) {
                    throw new ClcClientException(String.format(
                        "Start date must be within the past %s days",
                        config.MAX_HOURLY_PERIOD_DAYS)
                    );
                }

                if (config.getInterval().toHours() < config.MIN_HOURLY_INTERVAL_HOURS) {
                    throw new ClcClientException(String.format(
                        "Interval must be not less than %s hour(s)",
                        config.MIN_HOURLY_INTERVAL_HOURS)
                    );
                }

                break;
            }
            case REALTIME: {
                if (config.getInterval() == null) {
                    config.interval(config.DEFAULT_REALTIME_INTERVAL);
                }

                checkMonitoringFilter(config);

                if (config.getFrom().isBefore(OffsetDateTime.now().minusHours(config.MAX_REALTIME_PERIOD_HOURS))) {
                    throw new ClcClientException(String.format(
                        "Start date must be within the past %s hours",
                        config.MAX_REALTIME_PERIOD_HOURS)
                    );
                }

                if (config.getInterval().getSeconds() < config.MIN_REALTIME_INTERVAL_MINUTES * 60) {
                    throw new ClcClientException(String.format(
                        "Interval must be not less than %s minutes",
                        config.MIN_REALTIME_INTERVAL_MINUTES)
                    );
                }
                break;
            }
            case LATEST: {
                return request;
            }
        }

        return request
            .start(config.getFrom().atZoneSameInstant(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT))
            .end(
                config.getTo() != null
                    ? config.getTo().atZoneSameInstant(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT)
                    : null)
            .sampleInterval(buildInterval(config.getInterval()));
    }

    private String buildInterval(Duration duration) {

        long days = duration.toDays();
        long hours = duration.minusDays(days).toHours();
        long minutes = duration.minusDays(days).minusHours(hours).toMinutes();
        long seconds = duration.minusDays(days).minusHours(hours).minusMinutes(minutes).getSeconds();

        return String.format("%s:%s:%s:%s",
            days < 10 ? ("0" + days) : days,
            hours < 10 ? ("0" + hours) : hours,
            minutes < 10 ? ("0" + minutes) : minutes,
            seconds < 10 ? ("0" + seconds) : seconds
        );
    }

    private void checkMonitoringFilter(ServerMonitoringConfig config) {
        checkNotNull(config.getFrom(), "From date must be not a null");

        OffsetDateTime to = config.getTo() != null ? config.getTo() : OffsetDateTime.now();

        if (config.getFrom().isAfter(to)) {
            throw new ClcClientException("Start date cannot be more than end date");
        }
        if (Duration.between(config.getFrom(), to).getSeconds() < config.getInterval().getSeconds()) {
            throw new ClcClientException("Interval must fit within start/end date");
        }
    }
}

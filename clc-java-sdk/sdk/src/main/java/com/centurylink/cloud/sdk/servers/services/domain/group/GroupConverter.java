package com.centurylink.cloud.sdk.servers.services.domain.group;

import com.centurylink.cloud.sdk.servers.client.domain.group.*;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;

import java.util.ArrayList;
import java.util.List;

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
}

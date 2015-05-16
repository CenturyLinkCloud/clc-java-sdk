package com.centurylink.cloud.sdk.servers.services.domain.statistics;

import com.centurylink.cloud.sdk.common.management.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.common.management.services.DataCenterService;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.group.BillingStats;
import com.centurylink.cloud.sdk.servers.services.domain.group.GroupBilling;
import com.centurylink.cloud.sdk.servers.services.domain.group.ServerBilling;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@SuppressWarnings("unchecked")
public class BillingStatsEngine {

    private enum Filter {
        GROUP, SERVER, DATACENTER
    }

    private Filter filter = Filter.GROUP;

    private boolean withSubItems = false;

    private final GroupService groupService;
    private final DataCenterService dataCenterService;
    private final ServerService serverService;

    private GroupFilter groupFilter = new GroupFilter();
    private ServerFilter serverFilter;
    private DataCenterFilter dataCenterFilter;

    public BillingStatsEngine(
            ServerService serverService,
            GroupService groupService,
            DataCenterService dataCenterService
    ) {
        this.serverService = serverService;
        this.groupService = groupService;
        this.dataCenterService = dataCenterService;

    }

    public BillingStatsEngine forServers(ServerFilter serverFilter) {
        this.filter = Filter.SERVER;
        this.serverFilter = serverFilter;
        return this;
    }

    public BillingStatsEngine forGroups(GroupFilter groupFilter) {
        this.filter = Filter.GROUP;
        this.groupFilter = groupFilter;
        return this;
    }

    public BillingStatsEngine forDataCenters(DataCenterFilter dataCenterFilter) {
        this.filter = Filter.DATACENTER;
        this.dataCenterFilter = dataCenterFilter;
        return this;
    }

    public List<BillingStatsEntry> groupByGroup() {
        List<BillingStatsEntry> result = new ArrayList<>();

        getBillingStats().forEach(
            billingStats ->
                billingStats.getGroups().forEach(
                    groupBilling -> {
                        Statistics statistics = new Statistics();
                        aggregateStats(statistics, groupBilling);

                        result.add(
                            createBillingStatsEntry(
                                groupService.findByRef(
                                    Group.refById(groupBilling.getGroupId())
                                ),
                                statistics
                            )
                        );
                    }
                )
            );

        return result;
    }

    public List<BillingStatsEntry> groupByServer() {
        List<BillingStatsEntry> result = new ArrayList<>();

        getBillingStats().forEach(
            billingStats ->
                billingStats.getGroups().forEach(
                    groupBilling -> groupBilling.getServers().forEach(
                        serverBilling -> {
                            Statistics statistics = new Statistics();
                            aggregateStats(statistics, serverBilling);

                            result.add(
                                createBillingStatsEntry(
                                    serverService.findByRef(
                                        Server.refById(serverBilling.getServerId())
                                    ),
                                    statistics
                                )
                            );
                        }
                    )
                )
        );

        return result;
    }

    public List<BillingStatsEntry> groupByDataCenter() {
        List<BillingStatsEntry> result = new ArrayList<>();

        Map<DataCenterMetadata, Statistics> dataCenterMap = new HashMap<>();

        getBillingStats().forEach(
            billingStats -> billingStats.getGroups().forEach(
                groupBilling -> {
                    GroupMetadata groupMetadata = groupService.findByRef(
                        Group.refById(groupBilling.getGroupId())
                    );

                    DataCenterMetadata dataCenterMetadata = dataCenterService.findByRef(
                        DataCenter.refById(groupMetadata.getLocationId())
                    );

                    if (dataCenterMap.get(dataCenterMetadata) != null) {
                        aggregateStats(dataCenterMap.get(dataCenterMetadata), groupBilling);
                    } else {
                        Statistics statistics = new Statistics();
                        aggregateStats(statistics, groupBilling);
                        dataCenterMap.put(dataCenterMetadata, statistics);
                    }
                }
            )
        );

        dataCenterMap.forEach(
                (dataCenterMetadata, statistics) -> result.add(
                createBillingStatsEntry(dataCenterMetadata, statistics)
            )
        );

        return result;
    }

    public Statistics summarize() {
        Statistics statistics = new Statistics();

        getBillingStats().forEach(
            billingStats -> billingStats.getGroups().forEach(
                groupBilling -> aggregateStats(statistics, groupBilling)
            )
        );

        return statistics;
    }

    public BillingStatsEngine aggregateSubItems() {
        withSubItems = true;
        return this;
    }

    private GroupFilter getStatsSearchCriteria() {
        switch (filter) {
            case GROUP:
                return groupFilter;
            case DATACENTER:
                return
                    new GroupFilter()
                        .dataCentersWhere(dataCenterFilter);
            case SERVER:
                return
                    new GroupFilter().id(
                        serverService.find(serverFilter)
                            .stream()
                            .map(ServerMetadata::getGroupId)
                            .collect(toList())
                    );
            default:
                return new GroupFilter();
        }
    }

    private List<BillingStats> getBillingStats() {
        List<BillingStats> billingStatsList = groupService.getBillingStats(getStatsSearchCriteria());

        if (!withSubItems) {
            billingStatsList.forEach(
                billingStats -> {
                    GroupBilling groupBilling = billingStats
                        .getGroups()
                        .stream()
                        .findFirst()
                        .orElse(new GroupBilling());

                    List<GroupBilling> groupBillingList = new ArrayList<>();
                    groupBillingList.add(groupBilling);

                    billingStats.setGroups(groupBillingList);
                }
            );
        }

        return billingStatsList;
    }

    private void aggregateStats(Statistics statistics, GroupBilling groupBilling) {
        groupBilling.getServers().forEach(
            serverBilling -> aggregateStats(statistics, serverBilling)
        );
    }

    private void aggregateStats(Statistics statistics, ServerBilling serverBilling) {
        statistics
            .archiveCost(
                statistics.getArchiveCost().add(serverBilling.getArchiveCost())
            )
            .templateCost(
                statistics.getTemplateCost().add(serverBilling.getTemplateCost())
            )
            .monthlyEstimate(
                statistics.getMonthlyEstimate().add(serverBilling.getMonthlyEstimate())
            )
            .monthToDate(
                statistics.getMonthToDate().add(serverBilling.getMonthToDate())
            )
            .currentHour(
                statistics.getCurrentHour().add(serverBilling.getCurrentHour())
            );
    }

    private <T> BillingStatsEntry createBillingStatsEntry(T metadata, Statistics statistics) {
        return new BillingStatsEntry()
                .entity(metadata)
                .statistics(statistics);
    }
}

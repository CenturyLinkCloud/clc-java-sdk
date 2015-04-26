package com.centurylink.cloud.sdk.servers.services.domain.template.filters;

import com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.deployment.capabilities.TemplateMetadata;
import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenterRef;
import com.centurylink.cloud.sdk.core.services.filter.Filter;
import com.centurylink.cloud.sdk.core.services.function.Predicates;
import com.centurylink.cloud.sdk.servers.services.domain.os.CpuArchitecture;

import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.centurylink.cloud.sdk.core.services.function.Predicates.*;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Ilya Drabenia
 */
public class TemplateFilter implements Filter<TemplateFilter> {
    private DataCenterFilter dataCenter = new DataCenterFilter(alwaysTrue());
    private Predicate<TemplateMetadata> predicate = alwaysTrue();

    public TemplateFilter() {
    }

    private TemplateFilter(DataCenterFilter dataCenter, Predicate<TemplateMetadata> predicate) {
        this.dataCenter = dataCenter;
        this.predicate = predicate;
    }

    public TemplateFilter dataCenterWhere(Predicate<DataCenterMetadata> predicate) {
        dataCenter.where(predicate);
        return this;
    }

    public TemplateFilter dataCenters(String... ids) {
        dataCenter.id(ids);
        return this;
    }

    public TemplateFilter dataCenters(DataCenterRef... dataCenters) {
        dataCenter.in(dataCenters);

        return this;
    }

    public TemplateFilter dataCenterNameContains(String name) {
        dataCenter.nameContains(name);
        return this;
    }

    public TemplateFilter nameContains(String name) {
        checkNotNull(name, "Name must be not a null");

        predicate = predicate.and(combine(
            TemplateMetadata::getName, containsIgnoreCase(name)
        ));

        return this;
    }

    public TemplateFilter descriptionContains(String substring) {
        checkNotNull(substring, "Substring must be not a null");

        predicate = predicate.and(combine(
            TemplateMetadata::getDescription, containsIgnoreCase(substring)
        ));

        return this;
    }

    public TemplateFilter osTypes(OsFilter... osFilter) {
        checkNotNull(osFilter, "OS filter must be not a null");

        predicate = predicate.and(
            Stream.of(osFilter)
                .map(OsFilter::getPredicate)
                .reduce(alwaysFalse(), Predicate::or)
        );

        return this;
    }

    @Override
    public TemplateFilter and(TemplateFilter otherFilter) {
        return
            new TemplateFilter(
                getDataCenter().and(otherFilter.getDataCenter()),
                getPredicate().and(otherFilter.getPredicate())
            );
    }

    @Override
    public TemplateFilter or(TemplateFilter otherFilter) {
        return
            new TemplateFilter(
                getDataCenter().or(otherFilter.getDataCenter()),
                getPredicate().or(otherFilter.getPredicate())
            );
    }

    public DataCenterFilter getDataCenter() {
        return dataCenter;
    }

    public Predicate<TemplateMetadata> getPredicate() {
        return predicate;
    }
}

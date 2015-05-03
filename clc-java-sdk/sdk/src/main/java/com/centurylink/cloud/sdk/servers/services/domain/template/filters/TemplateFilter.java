package com.centurylink.cloud.sdk.servers.services.domain.template.filters;

import com.centurylink.cloud.sdk.common.management.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.common.management.client.domain.datacenters.deployment.capabilities.TemplateMetadata;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.function.Predicates;
import com.centurylink.cloud.sdk.core.services.filter.AbstractResourceFilter;
import com.centurylink.cloud.sdk.core.services.filter.evaluation.AndEvaluation;
import com.centurylink.cloud.sdk.core.services.filter.evaluation.OrEvaluation;
import com.centurylink.cloud.sdk.core.services.filter.evaluation.SingleFilterEvaluation;
import com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.OsFilter;

import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.centurylink.cloud.sdk.core.function.Predicates.*;
import static com.centurylink.cloud.sdk.core.preconditions.ArgumentPreconditions.allItemsNotNull;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.asList;

/**
 * Class that specify filter for search server templates
 *
 * @author Ilya Drabenia
 */
public class TemplateFilter extends AbstractResourceFilter<TemplateFilter> {
    private DataCenterFilter dataCenter = new DataCenterFilter(alwaysTrue());
    private Predicate<TemplateMetadata> predicate = alwaysTrue();

    public TemplateFilter() {
    }

    private TemplateFilter(DataCenterFilter dataCenter, Predicate<TemplateMetadata> predicate) {
        this.dataCenter = dataCenter;
        this.predicate = predicate;
    }

    /**
     * Method allow to provide custom filter predicate
     *
     * @param predicate
     * @return {@link TemplateFilter}
     * @throws java.lang.NullPointerException
     */
    public TemplateFilter dataCentersWhere(Predicate<DataCenterMetadata> predicate) {
        checkNotNull(predicate, "Predicate must be not a null");

        dataCenter.where(predicate);
        return this;
    }

    /**
     * Method allow to filter data centers by IDs. Filtering is strong case sensitive.
     *
     * @param ids
     * @return {@link TemplateFilter}
     */
    public TemplateFilter dataCenters(String... ids) {
        allItemsNotNull(ids, "Datacenter ID list");

        dataCenter.id(ids);

        return this;
    }

    /**
     * Method allow to filter data centers by references.
     *
     * @param dataCenters is list of references to target dataCenters
     * @return {@link TemplateFilter}
     * @throws java.lang.NullPointerException
     */
    public TemplateFilter dataCenters(DataCenter... dataCenters) {
        allItemsNotNull(dataCenters, "Datacenter references");

        dataCenter.dataCenters(dataCenters);

        return this;
    }

    /**
     * Method allow to filter data centers by name.
     * Filtering is case insensitive and occurs using substring search.
     *
     * @param names is a not null list of name substrings
     * @return {@link TemplateFilter}
     * @throws java.lang.NullPointerException
     */
    public TemplateFilter dataCenterNameContains(String... names) {
        allItemsNotNull(names, "Name substrings");

        dataCenter.nameContains(names);

        return this;
    }

    /**
     * Method allow to find templates that contains some substring in name.
     * Filtering is case insensitive.
     *
     * @param names is not null list of name substrings
     * @return {@link TemplateFilter}
     * @throws java.lang.NullPointerException
     */
    public TemplateFilter nameContains(String... names) {
        allItemsNotNull(names, "Name substrings");

        predicate = predicate.and(combine(
            TemplateMetadata::getName, in(asList(names), Predicates::containsIgnoreCase)
            ));

        return this;
    }

    /**
     * Method allow to find templates by its names
     * Filtering is case sensitive.
     *
     * @param names is a set of names
     * @return {@link TemplateFilter}
     */
    public TemplateFilter names(String... names) {
        allItemsNotNull(names, "Template names");

        predicate = predicate.and(combine(
            TemplateMetadata::getName, in(names)
        ));

        return this;
    }

    /**
     * Method allow to find templates that contains {@code substring} in description
     * Filtering is case insensitive.
     *
     * @param substrings is a set of descriptions
     * @return {@link TemplateFilter}
     */
    public TemplateFilter descriptionContains(String... substrings) {
        allItemsNotNull(substrings, "Template description substrings");

        predicate = predicate.and(combine(
            TemplateMetadata::getDescription, in(asList(substrings), Predicates::containsIgnoreCase)
        ));

        return this;
    }

    /**
     * Method allow to find templates with required image OS.
     *
     * @param osFilter is a not null instance of
     *                 {@link OsFilter}
     * @return {@link TemplateFilter}
     */
    public TemplateFilter osTypes(OsFilter... osFilter) {
        allItemsNotNull(osFilter, "OS templates");

        predicate = predicate.and(
            Stream.of(osFilter)
                .filter(notNull())
                .map(OsFilter::getPredicate)
                .reduce(alwaysFalse(), Predicate::or)
        );

        return this;
    }

    /**
     * Method allow to specify custom template filtering predicate
     *
     * @param predicate is not null filtering expression
     * @return {@link TemplateFilter}
     */
    public TemplateFilter where(Predicate<TemplateMetadata> predicate) {
        checkNotNull(predicate, "Predicate must be not a null");

        this.predicate = this.predicate.and(predicate);

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TemplateFilter and(TemplateFilter otherFilter) {
        checkNotNull(otherFilter, "Other filter must be not a null");

        if (filtersChain instanceof SingleFilterEvaluation &&
            otherFilter.filtersChain instanceof SingleFilterEvaluation) {
            return
                new TemplateFilter(
                    getDataCenter().and(otherFilter.getDataCenter()),
                    getPredicate().and(otherFilter.getPredicate())
                );
        } else {
            filtersChain = new AndEvaluation<>(filtersChain, otherFilter, TemplateMetadata::getName);

            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TemplateFilter or(TemplateFilter otherFilter) {
        checkNotNull(otherFilter, "Other filter must be not a null");

        filtersChain = new OrEvaluation<>(filtersChain, otherFilter, TemplateMetadata::getName);

        return this;
    }

    public DataCenterFilter getDataCenter() {
        return dataCenter;
    }

    public Predicate<TemplateMetadata> getPredicate() {
        return predicate;
    }
}

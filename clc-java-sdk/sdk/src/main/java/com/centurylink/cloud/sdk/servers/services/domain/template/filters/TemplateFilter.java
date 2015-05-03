package com.centurylink.cloud.sdk.servers.services.domain.template.filters;

import com.centurylink.cloud.sdk.common.services.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.common.services.client.domain.datacenters.deployment.capabilities.TemplateMetadata;
import com.centurylink.cloud.sdk.common.services.services.domain.datacenters.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.common.services.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.base.services.filter.AbstractResourceFilter;
import com.centurylink.cloud.sdk.base.services.filter.evaluation.AndEvaluation;
import com.centurylink.cloud.sdk.base.services.filter.evaluation.OrEvaluation;
import com.centurylink.cloud.sdk.base.services.filter.evaluation.SingleFilterEvaluation;
import com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.OsFilter;

import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.centurylink.cloud.sdk.base.function.Predicates.*;
import static com.google.common.base.Preconditions.checkNotNull;

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
        dataCenter.dataCenters(dataCenters);

        return this;
    }

    /**
     * Method allow to filter data centers by name.
     * Filtering is case insensitive and occurs using substring search.
     *
     * @param name is a not null name substring
     * @return {@link TemplateFilter}
     * @throws java.lang.NullPointerException
     */
    public TemplateFilter dataCenterNameContains(String name) {
        dataCenter.nameContains(name);
        return this;
    }

    /**
     * Method allow to find templates that contains some substring in name.
     * Filtering is case insensitive.
     *
     * @param name is not null name substring
     * @return {@link TemplateFilter}
     * @throws java.lang.NullPointerException
     */
    public TemplateFilter nameContains(String name) {
        checkNotNull(name, "Name must be not a null");

        predicate = predicate.and(combine(
            TemplateMetadata::getName, containsIgnoreCase(name)
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
        predicate = predicate.and(combine(
            TemplateMetadata::getName, in(names)
        ));

        return this;
    }

    /**
     * Method allow to find templates that contains {@code substring} in description
     * Filtering is case insensitive.
     *
     * @param substring is a set of names
     * @return {@link TemplateFilter}
     */
    public TemplateFilter descriptionContains(String substring) {
        checkNotNull(substring, "Substring must be not a null");

        predicate = predicate.and(combine(
            TemplateMetadata::getDescription, containsIgnoreCase(substring)
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

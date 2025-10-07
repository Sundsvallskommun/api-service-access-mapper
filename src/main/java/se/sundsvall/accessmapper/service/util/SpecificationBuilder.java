package se.sundsvall.accessmapper.service.util;

import static java.util.Objects.nonNull;

import org.springframework.data.jpa.domain.Specification;
import se.sundsvall.accessmapper.integration.db.model.AccessGroupEntity;

public class SpecificationBuilder<T> {

	private static final SpecificationBuilder<AccessGroupEntity> ACCESS_GROUP_ENTITY_SPECIFICATION_BUILDER = new SpecificationBuilder<>();

	public static Specification<AccessGroupEntity> withNamespace(final String namespace) {
		return ACCESS_GROUP_ENTITY_SPECIFICATION_BUILDER.buildEqualFilter("namespace", namespace);
	}

	public static Specification<AccessGroupEntity> withMunicipalityId(final String municipalityId) {
		return ACCESS_GROUP_ENTITY_SPECIFICATION_BUILDER.buildEqualFilter("municipalityId", municipalityId);
	}

	public static Specification<AccessGroupEntity> withAccessType(final String type) {
		return ACCESS_GROUP_ENTITY_SPECIFICATION_BUILDER.buildAccessTypeFilter(type);
	}

	/**
	 * Method builds an equal filter if the value is not null. If value is null, the method returns an always-true predicate
	 * (meaning no filtering will be applied for sent in attribute)
	 *
	 * @param  attribute name that will be used in filter
	 * @param  value     value (or null) to compare against
	 * @return           Specification<T> matching sent in comparison
	 */
	private Specification<T> buildEqualFilter(final String attribute, final Object value) {
		return (entity, cq, cb) -> nonNull(value) ? cb.equal(entity.get(attribute), value) : cb.and();
	}

	private Specification<T> buildAccessTypeFilter(final String type) {
		return (entity, cq, cb) -> {
			if (type == null) {
				return cb.and();
			}
			return cb.equal(entity.join("accessByType").get("type"), type);
		};
	}
}

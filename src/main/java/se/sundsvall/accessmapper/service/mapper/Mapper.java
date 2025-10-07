package se.sundsvall.accessmapper.service.mapper;

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Optional;
import se.sundsvall.accessmapper.api.model.Access;
import se.sundsvall.accessmapper.api.model.AccessGroup;
import se.sundsvall.accessmapper.api.model.AccessLevel;
import se.sundsvall.accessmapper.api.model.AccessType;
import se.sundsvall.accessmapper.integration.db.model.AccessEntity;
import se.sundsvall.accessmapper.integration.db.model.AccessGroupEntity;
import se.sundsvall.accessmapper.integration.db.model.AccessTypeEntity;

public final class Mapper {

	private Mapper() {}

	public static List<AccessGroup> toAccessGroups(final List<AccessGroupEntity> entities) {
		return Optional.ofNullable(entities).orElse(emptyList()).stream().map(Mapper::toAccessGroup).toList();
	}

	public static AccessGroup toAccessGroup(final AccessGroupEntity entity) {
		return AccessGroup.create()
			.withAccessByType(toAccessTypes(entity.getAccessByType()))
			.withGroup(entity.getId());
	}

	public static List<AccessType> toAccessTypes(final List<AccessTypeEntity> entityList) {
		return Optional.ofNullable(entityList).orElse(emptyList()).stream().map(Mapper::toAccessType).toList();
	}

	public static AccessType toAccessType(final AccessTypeEntity entity) {
		return AccessType.create()
			.withType(entity.getType())
			.withAccess(toAccessList(entity.getAccess()));
	}

	public static List<Access> toAccessList(final List<AccessEntity> entityList) {
		return Optional.ofNullable(entityList).orElse(emptyList()).stream().map(Mapper::toAccess).toList();
	}

	public static Access toAccess(final AccessEntity entity) {
		return Access.create()
			.withAccessLevel(AccessLevel.valueOf(entity.getAccessLevel()))
			.withPattern(entity.getPattern());
	}

	public static AccessGroupEntity toAccessGroupEntity(final String municipalityId, final String namespace, final String groupId, final AccessGroup accessGroup) {
		return AccessGroupEntity.create()
			.withNamespace(namespace)
			.withMunicipalityId(municipalityId)
			.withId(groupId)
			.withAccessByType(toAccessTypeEntities(accessGroup.getAccessByType()));

	}

	public static List<AccessTypeEntity> toAccessTypeEntities(final List<AccessType> entityList) {
		return Optional.ofNullable(entityList).orElse(emptyList()).stream().map(Mapper::toAccessTypeEntity).toList();
	}

	private static AccessTypeEntity toAccessTypeEntity(final AccessType accessType) {
		return AccessTypeEntity.create()
			.withType(accessType.getType())
			.withAccess(toAccessEntities(accessType.getAccess()));
	}

	private static List<AccessEntity> toAccessEntities(final List<Access> access) {
		return Optional.ofNullable(access).orElse(emptyList()).stream().map(Mapper::toAccessEntity).toList();

	}

	private static AccessEntity toAccessEntity(final Access access) {
		return AccessEntity.create()
			.withAccessLevel(access.getAccessLevel().name())
			.withPattern(access.getPattern());
	}

}

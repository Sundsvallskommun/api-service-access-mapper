package se.sundsvall.accessmapper.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import se.sundsvall.accessmapper.api.model.Access;
import se.sundsvall.accessmapper.api.model.AccessGroup;
import se.sundsvall.accessmapper.api.model.AccessLevel;
import se.sundsvall.accessmapper.api.model.AccessType;
import se.sundsvall.accessmapper.integration.db.model.AccessEntity;
import se.sundsvall.accessmapper.integration.db.model.AccessGroupEntity;
import se.sundsvall.accessmapper.integration.db.model.AccessTypeEntity;

class MapperTest {

	private static final String NAMESPACE = "namespace";
	private static final String MUNICIPALITY_ID = "municipalityId";
	private static final String GROUP_ID = "groupId";
	private static final String TYPE = "type";
	private static final String PATTERN = "pattern";

	@Test
	void toAccessGroups() {
		// Arrange
		final var entity1 = AccessGroupEntity.create()
			.withId("group1")
			.withMunicipalityId(MUNICIPALITY_ID)
			.withNamespace(NAMESPACE)
			.withAccessByType(List.of(AccessTypeEntity.create()
				.withType(TYPE)
				.withAccess(List.of(AccessEntity.create()
					.withPattern(PATTERN)
					.withAccessLevel(AccessLevel.LR.name())))));

		final var entity2 = AccessGroupEntity.create()
			.withId("group2")
			.withMunicipalityId(MUNICIPALITY_ID)
			.withNamespace(NAMESPACE)
			.withAccessByType(List.of(AccessTypeEntity.create()
				.withType(TYPE)
				.withAccess(List.of(AccessEntity.create()
					.withPattern(PATTERN)
					.withAccessLevel(AccessLevel.RW.name())))));

		// Call
		final var response = Mapper.toAccessGroups(List.of(entity1, entity2));

		// Assertions
		assertThat(response).isNotNull().hasSize(2);
		assertThat(response.getFirst().getGroup()).isEqualTo("group1");
		assertThat(response.getLast().getGroup()).isEqualTo("group2");
	}

	@Test
	void toAccessGroupsWithNull() {
		// Act
		final var response = Mapper.toAccessGroups(null);

		// Assert
		assertThat(response).isNotNull().isEmpty();
	}

	@Test
	void toAccessGroupsWithEmptyList() {
		// Act
		final var response = Mapper.toAccessGroups(Collections.emptyList());

		// Assert
		assertThat(response).isNotNull().isEmpty();
	}

	@Test
	void toAccessGroup() {
		// Arrange
		final var entity = AccessGroupEntity.create()
			.withId(GROUP_ID)
			.withMunicipalityId(MUNICIPALITY_ID)
			.withNamespace(NAMESPACE)
			.withAccessByType(List.of(AccessTypeEntity.create()
				.withType(TYPE)
				.withAccess(List.of(AccessEntity.create()
					.withPattern(PATTERN)
					.withAccessLevel(AccessLevel.LR.name())))));

		// Call
		final var response = Mapper.toAccessGroup(entity);

		// Assertions
		assertThat(response).isNotNull();
		assertThat(response.getGroup()).isEqualTo(GROUP_ID);
		assertThat(response.getAccessByType()).hasSize(1);
		assertThat(response.getAccessByType().getFirst().getType()).isEqualTo(TYPE);
	}

	@Test
	void toAccessGroupWithNullAccessByType() {
		// Arrange
		final var entity = AccessGroupEntity.create()
			.withId(GROUP_ID)
			.withMunicipalityId(MUNICIPALITY_ID)
			.withNamespace(NAMESPACE)
			.withAccessByType(null);

		// Call
		final var response = Mapper.toAccessGroup(entity);

		// Assertions
		assertThat(response).isNotNull();
		assertThat(response.getGroup()).isEqualTo(GROUP_ID);
		assertThat(response.getAccessByType()).isNotNull().isEmpty();
	}

	@Test
	void toAccessTypes() {
		// Arrange
		final var entity1 = AccessTypeEntity.create()
			.withType("type1")
			.withAccess(List.of(AccessEntity.create()
				.withPattern(PATTERN)
				.withAccessLevel(AccessLevel.LR.name())));

		final var entity2 = AccessTypeEntity.create()
			.withType("type2")
			.withAccess(List.of(AccessEntity.create()
				.withPattern(PATTERN)
				.withAccessLevel(AccessLevel.RW.name())));

		// Act
		final var response = Mapper.toAccessTypes(List.of(entity1, entity2));

		// Assert
		assertThat(response).isNotNull().hasSize(2);
		assertThat(response.getFirst().getType()).isEqualTo("type1");
		assertThat(response.getLast().getType()).isEqualTo("type2");
	}

	@Test
	void toAccessTypesWithNull() {
		// Act
		final var response = Mapper.toAccessTypes(null);

		// Assert
		assertThat(response).isNotNull().isEmpty();
	}

	@Test
	void toAccessTypesWithEmptyList() {
		// Act
		final var response = Mapper.toAccessTypes(Collections.emptyList());

		// Assert
		assertThat(response).isNotNull().isEmpty();
	}

	@Test
	void toAccessType() {
		// Arrange
		final var entity = AccessTypeEntity.create()
			.withType(TYPE)
			.withAccess(List.of(AccessEntity.create()
				.withPattern(PATTERN)
				.withAccessLevel(AccessLevel.LR.name())));

		// Act
		final var response = Mapper.toAccessType(entity);

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getType()).isEqualTo(TYPE);
		assertThat(response.getAccess()).hasSize(1);
		assertThat(response.getAccess().getFirst().getPattern()).isEqualTo(PATTERN);
		assertThat(response.getAccess().getFirst().getAccessLevel()).isEqualTo(AccessLevel.LR);
	}

	@Test
	void toAccessTypeWithNullAccess() {
		// Arrange
		final var entity = AccessTypeEntity.create()
			.withType(TYPE)
			.withAccess(null);

		// Act
		final var response = Mapper.toAccessType(entity);

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getType()).isEqualTo(TYPE);
		assertThat(response.getAccess()).isNotNull().isEmpty();
	}

	@Test
	void toAccessList() {
		// Arrange
		final var entity1 = AccessEntity.create()
			.withPattern("pattern1")
			.withAccessLevel(AccessLevel.LR.name());

		final var entity2 = AccessEntity.create()
			.withPattern("pattern2")
			.withAccessLevel(AccessLevel.RW.name());

		// Act
		final var response = Mapper.toAccessList(List.of(entity1, entity2));

		// Assert
		assertThat(response).isNotNull().hasSize(2);
		assertThat(response.getFirst().getPattern()).isEqualTo("pattern1");
		assertThat(response.getFirst().getAccessLevel()).isEqualTo(AccessLevel.LR);
		assertThat(response.getLast().getPattern()).isEqualTo("pattern2");
		assertThat(response.getLast().getAccessLevel()).isEqualTo(AccessLevel.RW);
	}

	@Test
	void toAccessListWithNull() {
		// Act
		final var response = Mapper.toAccessList(null);

		// Assert
		assertThat(response).isNotNull().isEmpty();
	}

	@Test
	void toAccessListWithEmptyList() {
		// Act
		final var response = Mapper.toAccessList(Collections.emptyList());

		// Assert
		assertThat(response).isNotNull().isEmpty();
	}

	@Test
	void toAccess() {
		// Arrange
		final var entity = AccessEntity.create()
			.withPattern(PATTERN)
			.withAccessLevel(AccessLevel.LR.name());

		// Act
		final var response = Mapper.toAccess(entity);

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getPattern()).isEqualTo(PATTERN);
		assertThat(response.getAccessLevel()).isEqualTo(AccessLevel.LR);
	}

	@Test
	void toAccessWithAllAccessLevels() {
		// Arrange & Act & Assert - LR
		var entity = AccessEntity.create()
			.withPattern(PATTERN)
			.withAccessLevel(AccessLevel.LR.name());
		var response = Mapper.toAccess(entity);
		assertThat(response.getAccessLevel()).isEqualTo(AccessLevel.LR);

		// Arrange & Act & Assert - RW
		entity = AccessEntity.create()
			.withPattern(PATTERN)
			.withAccessLevel(AccessLevel.RW.name());
		response = Mapper.toAccess(entity);
		assertThat(response.getAccessLevel()).isEqualTo(AccessLevel.RW);
	}

	@Test
	void toAccessGroupEntity() {
		// Arrange
		final var accessGroup = AccessGroup.create()
			.withGroup(GROUP_ID)
			.withAccessByType(List.of(AccessType.create()
				.withType(TYPE)
				.withAccess(List.of(Access.create()
					.withPattern(PATTERN)
					.withAccessLevel(AccessLevel.RW)))));

		// Act
		final var response = Mapper.toAccessGroupEntity(MUNICIPALITY_ID, NAMESPACE, GROUP_ID, accessGroup);

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getId()).isEqualTo(GROUP_ID);
		assertThat(response.getMunicipalityId()).isEqualTo(MUNICIPALITY_ID);
		assertThat(response.getNamespace()).isEqualTo(NAMESPACE);
		assertThat(response.getAccessByType()).hasSize(1);
		assertThat(response.getAccessByType().getFirst().getType()).isEqualTo(TYPE);
		assertThat(response.getAccessByType().getFirst().getAccess()).hasSize(1);
		assertThat(response.getAccessByType().getFirst().getAccess().getFirst().getPattern()).isEqualTo(PATTERN);
		assertThat(response.getAccessByType().getFirst().getAccess().getFirst().getAccessLevel()).isEqualTo(AccessLevel.RW.name());
	}

	@Test
	void toAccessGroupEntityWithNullAccessByType() {
		// Arrange
		final var accessGroup = AccessGroup.create()
			.withGroup(GROUP_ID)
			.withAccessByType(null);

		// Act
		final var response = Mapper.toAccessGroupEntity(MUNICIPALITY_ID, NAMESPACE, GROUP_ID, accessGroup);

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getId()).isEqualTo(GROUP_ID);
		assertThat(response.getMunicipalityId()).isEqualTo(MUNICIPALITY_ID);
		assertThat(response.getNamespace()).isEqualTo(NAMESPACE);
		assertThat(response.getAccessByType()).isNotNull().isEmpty();
	}

	@Test
	void toAccessGroupEntityWithEmptyAccessByType() {
		// Arrange
		final var accessGroup = AccessGroup.create()
			.withGroup(GROUP_ID)
			.withAccessByType(Collections.emptyList());

		// Act
		final var response = Mapper.toAccessGroupEntity(MUNICIPALITY_ID, NAMESPACE, GROUP_ID, accessGroup);

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getId()).isEqualTo(GROUP_ID);
		assertThat(response.getMunicipalityId()).isEqualTo(MUNICIPALITY_ID);
		assertThat(response.getNamespace()).isEqualTo(NAMESPACE);
		assertThat(response.getAccessByType()).isNotNull().isEmpty();
	}

	@Test
	void toAccessTypeEntities() {
		// Arrange
		final var accessType1 = AccessType.create()
			.withType("type1")
			.withAccess(List.of(Access.create()
				.withPattern(PATTERN)
				.withAccessLevel(AccessLevel.LR)));

		final var accessType2 = AccessType.create()
			.withType("type2")
			.withAccess(List.of(Access.create()
				.withPattern(PATTERN)
				.withAccessLevel(AccessLevel.RW)));

		// Act
		final var response = Mapper.toAccessTypeEntities(List.of(accessType1, accessType2));

		// Assert
		assertThat(response).isNotNull().hasSize(2);
		assertThat(response.getFirst().getType()).isEqualTo("type1");
		assertThat(response.getLast().getType()).isEqualTo("type2");
	}

	@Test
	void toAccessTypeEntitiesWithNull() {
		// Act
		final var response = Mapper.toAccessTypeEntities(null);

		// Assert
		assertThat(response).isNotNull().isEmpty();
	}

	@Test
	void toAccessTypeEntitiesWithEmptyList() {
		// Act
		final var response = Mapper.toAccessTypeEntities(Collections.emptyList());

		// Assert
		assertThat(response).isNotNull().isEmpty();
	}

	@Test
	void toAccessGroupEntityRoundTrip() {
		// Arrange
		final var originalEntity = AccessGroupEntity.create()
			.withId(GROUP_ID)
			.withMunicipalityId(MUNICIPALITY_ID)
			.withNamespace(NAMESPACE)
			.withAccessByType(List.of(AccessTypeEntity.create()
				.withType(TYPE)
				.withAccess(List.of(AccessEntity.create()
					.withPattern(PATTERN)
					.withAccessLevel(AccessLevel.RW.name())))));

		// Act
		final var accessGroup = Mapper.toAccessGroup(originalEntity);
		final var resultEntity = Mapper.toAccessGroupEntity(MUNICIPALITY_ID, NAMESPACE, GROUP_ID, accessGroup);

		// Assert
		assertThat(resultEntity).isNotNull();
		assertThat(resultEntity.getId()).isEqualTo(originalEntity.getId());
		assertThat(resultEntity.getMunicipalityId()).isEqualTo(originalEntity.getMunicipalityId());
		assertThat(resultEntity.getNamespace()).isEqualTo(originalEntity.getNamespace());
		assertThat(resultEntity.getAccessByType()).hasSize(1);
		assertThat(resultEntity.getAccessByType().getFirst().getType()).isEqualTo(TYPE);
		assertThat(resultEntity.getAccessByType().getFirst().getAccess()).hasSize(1);
		assertThat(resultEntity.getAccessByType().getFirst().getAccess().getFirst().getPattern()).isEqualTo(PATTERN);
		assertThat(resultEntity.getAccessByType().getFirst().getAccess().getFirst().getAccessLevel()).isEqualTo(AccessLevel.RW.name());
	}

	@Test
	void toAccessGroupWithComplexStructure() {
		// Arrange
		final var entity = AccessGroupEntity.create()
			.withId(GROUP_ID)
			.withMunicipalityId(MUNICIPALITY_ID)
			.withNamespace(NAMESPACE)
			.withAccessByType(List.of(
				AccessTypeEntity.create()
					.withType("type1")
					.withAccess(List.of(
						AccessEntity.create()
							.withPattern("pattern1")
							.withAccessLevel(AccessLevel.LR.name()),
						AccessEntity.create()
							.withPattern("pattern2")
							.withAccessLevel(AccessLevel.RW.name()))),
				AccessTypeEntity.create()
					.withType("type2")
					.withAccess(List.of(
						AccessEntity.create()
							.withPattern("pattern3")
							.withAccessLevel(AccessLevel.LR.name())))));

		// Act
		final var response = Mapper.toAccessGroup(entity);

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getGroup()).isEqualTo(GROUP_ID);
		assertThat(response.getAccessByType()).hasSize(2);
		assertThat(response.getAccessByType().getFirst().getType()).isEqualTo("type1");
		assertThat(response.getAccessByType().getFirst().getAccess()).hasSize(2);
		assertThat(response.getAccessByType().getLast().getType()).isEqualTo("type2");
		assertThat(response.getAccessByType().getLast().getAccess()).hasSize(1);
	}

	@Test
	void toAccessGroupEntityWithComplexStructure() {
		// Arrange
		final var accessGroup = AccessGroup.create()
			.withGroup(GROUP_ID)
			.withAccessByType(List.of(
				AccessType.create()
					.withType("type1")
					.withAccess(List.of(
						Access.create()
							.withPattern("pattern1")
							.withAccessLevel(AccessLevel.LR),
						Access.create()
							.withPattern("pattern2")
							.withAccessLevel(AccessLevel.RW))),
				AccessType.create()
					.withType("type2")
					.withAccess(List.of(
						Access.create()
							.withPattern("pattern3")
							.withAccessLevel(AccessLevel.LR)))));

		// Act
		final var response = Mapper.toAccessGroupEntity(MUNICIPALITY_ID, NAMESPACE, GROUP_ID, accessGroup);

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getId()).isEqualTo(GROUP_ID);
		assertThat(response.getMunicipalityId()).isEqualTo(MUNICIPALITY_ID);
		assertThat(response.getNamespace()).isEqualTo(NAMESPACE);
		assertThat(response.getAccessByType()).hasSize(2);
		assertThat(response.getAccessByType().getFirst().getType()).isEqualTo("type1");
		assertThat(response.getAccessByType().getFirst().getAccess()).hasSize(2);
		assertThat(response.getAccessByType().getLast().getType()).isEqualTo("type2");
		assertThat(response.getAccessByType().getLast().getAccess()).hasSize(1);
	}
}

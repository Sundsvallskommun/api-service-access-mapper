package se.sundsvall.accessmapper.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.zalando.problem.Status.CONFLICT;
import static org.zalando.problem.Status.NOT_FOUND;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zalando.problem.ThrowableProblem;
import se.sundsvall.accessmapper.api.model.Access;
import se.sundsvall.accessmapper.api.model.AccessGroup;
import se.sundsvall.accessmapper.api.model.AccessLevel;
import se.sundsvall.accessmapper.api.model.AccessType;
import se.sundsvall.accessmapper.integration.db.AccessGroupRepository;
import se.sundsvall.accessmapper.integration.db.model.AccessEntity;
import se.sundsvall.accessmapper.integration.db.model.AccessGroupEntity;
import se.sundsvall.accessmapper.integration.db.model.AccessTypeEntity;

@ExtendWith(MockitoExtension.class)
class AccessGroupServiceTest {

	private static final String NAMESPACE = "namespace";

	private static final String MUNICIPALITY_ID = "municipalityId";

	private static final String GROUP_ID = "groupId";

	private static final String TYPE = "type";

	@Mock
	private AccessGroupRepository accessGroupRepositoryMock;

	@InjectMocks
	private AccessGroupService service;

	@Test
	void getAccessGroup() {
		// Arrange
		final var entity = AccessGroupEntity.create()
			.withId(GROUP_ID)
			.withMunicipalityId(MUNICIPALITY_ID)
			.withNamespace(NAMESPACE)
			.withAccessByType(List.of(AccessTypeEntity
				.create()
				.withType(TYPE)
				.withAccess(List.of(AccessEntity
					.create()
					.withPattern("pattern")
					.withAccessLevel(AccessLevel.LR.name())))));

		when(accessGroupRepositoryMock.findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, GROUP_ID))
			.thenReturn(entity);

		// Act
		final var response = service.getAccessGroup(MUNICIPALITY_ID, NAMESPACE, GROUP_ID);

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getGroup()).isEqualTo(GROUP_ID);

		verify(accessGroupRepositoryMock).findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, GROUP_ID);
	}

	@Test
	void getAccessGroupNotFound() {

		when(accessGroupRepositoryMock.findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, GROUP_ID))
			.thenReturn(null);

		// Act
		final var exception = assertThrows(ThrowableProblem.class,
			() -> service.getAccessGroup(MUNICIPALITY_ID, NAMESPACE, GROUP_ID));

		// Assert
		assertThat(exception.getStatus()).isEqualTo(NOT_FOUND);
		assertThat(exception.getTitle()).isEqualTo(NOT_FOUND.getReasonPhrase());
		assertThat(exception.getMessage()).isEqualTo("Not Found: Access group not found for municipalityId: municipalityId, namespace: namespace, groupId: groupId.");

		verify(accessGroupRepositoryMock).findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, GROUP_ID);
	}

	@Test
	void getAccessGroupsWithMatches() {
		// Arrange
		final var entity1 = AccessGroupEntity.create()
			.withId("group1")
			.withMunicipalityId(MUNICIPALITY_ID)
			.withNamespace(NAMESPACE)
			.withAccessByType(List.of(AccessTypeEntity
				.create()
				.withType(TYPE)
				.withAccess(List.of(AccessEntity
					.create()
					.withPattern("pattern")
					.withAccessLevel(AccessLevel.LR.name())))));
		final var entity2 = AccessGroupEntity.create()
			.withId("group2")
			.withMunicipalityId(MUNICIPALITY_ID)
			.withNamespace(NAMESPACE)
			.withAccessByType(List.of(AccessTypeEntity
				.create()
				.withType(TYPE)
				.withAccess(List.of(AccessEntity
					.create()
					.withPattern("pattern")
					.withAccessLevel(AccessLevel.LR.name())))));

		when(accessGroupRepositoryMock.findByMunicipalityIdAndNamespaceAndAccessByType_Type(MUNICIPALITY_ID, NAMESPACE, TYPE))
			.thenReturn(List.of(entity1, entity2));

		// Act
		final var response = service.getAccessGroups(MUNICIPALITY_ID, NAMESPACE, TYPE);

		// Assert
		assertThat(response).isNotNull().hasSize(2);
		assertThat(response.getFirst().getGroup()).isEqualTo("group1");
		assertThat(response.getLast().getGroup()).isEqualTo("group2");

		verify(accessGroupRepositoryMock).findByMunicipalityIdAndNamespaceAndAccessByType_Type(MUNICIPALITY_ID, NAMESPACE, TYPE);
	}

	@Test
	void getAccessGroupsWithoutMatches() {

		when(accessGroupRepositoryMock.findByMunicipalityIdAndNamespaceAndAccessByType_Type(MUNICIPALITY_ID, NAMESPACE, TYPE))
			.thenReturn(List.of());

		// Act
		final var response = service.getAccessGroups(MUNICIPALITY_ID, NAMESPACE, TYPE);

		// Assert
		assertThat(response).isNotNull().isEmpty();

		verify(accessGroupRepositoryMock).findByMunicipalityIdAndNamespaceAndAccessByType_Type(MUNICIPALITY_ID, NAMESPACE, TYPE);
	}

	@Test
	void createAccessGroup() {
		// Arrange
		final var accessGroup = AccessGroup.create().withGroup(GROUP_ID)
			.withAccessByType(List.of(AccessType
				.create()
				.withType(TYPE)
				.withAccess(List.of(Access
					.create()
					.withPattern("pattern")
					.withAccessLevel(AccessLevel.RW)))));

		final var entity = AccessGroupEntity.create()
			.withId(GROUP_ID)
			.withMunicipalityId(MUNICIPALITY_ID)
			.withNamespace(NAMESPACE);

		when(accessGroupRepositoryMock.existsByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, GROUP_ID)).thenReturn(false);
		when(accessGroupRepositoryMock.save(any(AccessGroupEntity.class))).thenReturn(entity);

		// Act
		service.createAccessGroup(MUNICIPALITY_ID, NAMESPACE, GROUP_ID, accessGroup);

		// Assert
		verify(accessGroupRepositoryMock).save(any(AccessGroupEntity.class));
	}

	@Test
	void createAccessGroupWithExistingId() {
		// Arrange
		when(accessGroupRepositoryMock.existsByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, GROUP_ID))
			.thenReturn(true);
		final var accessGroup = AccessGroup.create().withGroup(GROUP_ID);

		// Act
		final var exception = assertThrows(ThrowableProblem.class,
			() -> service.createAccessGroup(MUNICIPALITY_ID, NAMESPACE, GROUP_ID, accessGroup));

		// Assert
		assertThat(exception.getStatus()).isEqualTo(CONFLICT);
		assertThat(exception.getTitle()).isEqualTo(CONFLICT.getReasonPhrase());
		assertThat(exception.getMessage()).isEqualTo("Conflict: Access group already exists for municipalityId: municipalityId, namespace: namespace, groupId: groupId.");

	}

	@Test
	void updateExistingAccessGroup() {
		// Arrange
		final var accessGroup = AccessGroup.create().withGroup(GROUP_ID)
			.withAccessByType(List.of(AccessType
				.create()
				.withType(TYPE)
				.withAccess(List.of(Access
					.create()
					.withPattern("pattern")
					.withAccessLevel(AccessLevel.RW)))));
		final var existingEntity = AccessGroupEntity.create()
			.withId(GROUP_ID)
			.withMunicipalityId(MUNICIPALITY_ID)
			.withNamespace(NAMESPACE)
			.withAccessByType(List.of(AccessTypeEntity
				.create()
				.withType(TYPE)
				.withAccess(List.of(AccessEntity
					.create()
					.withPattern("pattern")
					.withAccessLevel(AccessLevel.LR.name())))));

		when(accessGroupRepositoryMock.findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, GROUP_ID))
			.thenReturn(existingEntity);
		when(accessGroupRepositoryMock.save(any(AccessGroupEntity.class))).thenReturn(existingEntity);

		// Act
		service.updateAccessGroup(MUNICIPALITY_ID, NAMESPACE, GROUP_ID, accessGroup);

		// Assert
		verify(accessGroupRepositoryMock).findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, GROUP_ID);
		verify(accessGroupRepositoryMock).save(any(AccessGroupEntity.class));
	}

	@Test
	void updateNonExistingAccessGroup() {
		// Arrange
		final var accessGroup = AccessGroup.create().withGroup(GROUP_ID);

		when(accessGroupRepositoryMock.findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, GROUP_ID))
			.thenReturn(null);

		// Act
		final var exception = assertThrows(ThrowableProblem.class,
			() -> service.updateAccessGroup(MUNICIPALITY_ID, NAMESPACE, GROUP_ID, accessGroup));

		// Assert
		assertThat(exception.getStatus()).isEqualTo(NOT_FOUND);
		assertThat(exception.getTitle()).isEqualTo(NOT_FOUND.getReasonPhrase());
		assertThat(exception.getMessage()).isEqualTo("Not Found: Access group not found for municipalityId: municipalityId, namespace: namespace, groupId: groupId.");

		verify(accessGroupRepositoryMock).findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, GROUP_ID);
	}

	@Test
	void deleteExistingAccessGroup() {
		// Arrange
		final var entity = AccessGroupEntity.create()
			.withId(GROUP_ID)
			.withMunicipalityId(MUNICIPALITY_ID)
			.withNamespace(NAMESPACE);

		when(accessGroupRepositoryMock.findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, GROUP_ID))
			.thenReturn(entity);

		// Act
		service.deleteAccessGroup(MUNICIPALITY_ID, NAMESPACE, GROUP_ID);

		// Assert
		verify(accessGroupRepositoryMock).findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, GROUP_ID);
		verify(accessGroupRepositoryMock).delete(entity);
	}

	@Test
	void deleteNonExistingAccessGroup() {

		when(accessGroupRepositoryMock.findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, GROUP_ID))
			.thenReturn(null);

		// Act
		final var exception = assertThrows(ThrowableProblem.class,
			() -> service.deleteAccessGroup(MUNICIPALITY_ID, NAMESPACE, GROUP_ID));

		// Assert
		assertThat(exception.getStatus()).isEqualTo(NOT_FOUND);
		assertThat(exception.getTitle()).isEqualTo(NOT_FOUND.getReasonPhrase());
		assertThat(exception.getMessage()).isEqualTo("Not Found: Access group not found for municipalityId: municipalityId, namespace: namespace, groupId: groupId.");

		verify(accessGroupRepositoryMock).findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, GROUP_ID);
	}

	@Test
	void getAccessGroupEntity() {
		// Arrange
		final var entity = AccessGroupEntity.create()
			.withId(GROUP_ID)
			.withMunicipalityId(MUNICIPALITY_ID)
			.withNamespace(NAMESPACE);

		when(accessGroupRepositoryMock.findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, GROUP_ID))
			.thenReturn(entity);

		// Act
		final var response = service.getAccessGroupEntity(MUNICIPALITY_ID, NAMESPACE, GROUP_ID);

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getId()).isEqualTo(GROUP_ID);
		assertThat(response.getMunicipalityId()).isEqualTo(MUNICIPALITY_ID);
		assertThat(response.getNamespace()).isEqualTo(NAMESPACE);

		verify(accessGroupRepositoryMock).findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, GROUP_ID);
	}

	@Test
	void getAccessGroupEntityNotFound() {

		// Arrange
		when(accessGroupRepositoryMock.findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, GROUP_ID))
			.thenReturn(null);

		// Act
		final var exception = assertThrows(ThrowableProblem.class,
			() -> service.getAccessGroupEntity(MUNICIPALITY_ID, NAMESPACE, GROUP_ID));

		// Assert
		assertThat(exception.getStatus()).isEqualTo(NOT_FOUND);
		assertThat(exception.getTitle()).isEqualTo(NOT_FOUND.getReasonPhrase());
		assertThat(exception.getMessage()).isEqualTo("Not Found: Access group not found for municipalityId: municipalityId, namespace: namespace, groupId: groupId.");

		verify(accessGroupRepositoryMock).findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, GROUP_ID);
	}

	@AfterEach
	void verifyNoMoreInteractionsOnMocks() {
		verifyNoMoreInteractions(accessGroupRepositoryMock);
	}
}

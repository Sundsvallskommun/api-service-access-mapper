package se.sundsvall.accessmapper.service;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sundsvall.accessmapper.api.model.Access;
import se.sundsvall.accessmapper.api.model.AccessLevel;
import se.sundsvall.accessmapper.api.model.AccessType;
import se.sundsvall.accessmapper.api.model.AccessUser;
import se.sundsvall.accessmapper.integration.db.AccessUserRepository;
import se.sundsvall.accessmapper.integration.db.model.AccessEntity;
import se.sundsvall.accessmapper.integration.db.model.AccessTypeEntity;
import se.sundsvall.accessmapper.integration.db.model.AccessUserEntity;
import se.sundsvall.dept44.problem.ThrowableProblem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class AccessUserServiceTest {

	private static final String NAMESPACE = "namespace";

	private static final String MUNICIPALITY_ID = "municipalityId";

	private static final String ID = "id";

	private static final String USER_ID = "userId";

	private static final String TYPE = "type";

	@Mock
	private AccessUserRepository accessUserRepositoryMock;

	@InjectMocks
	private AccessUserService service;

	@Test
	void getAccessUsers() {
		// Arrange
		final var entity = AccessUserEntity.create()
			.withId(ID)
			.withMunicipalityId(MUNICIPALITY_ID)
			.withNamespace(NAMESPACE)
			.withUserId(USER_ID)
			.withAccessByType(List.of(AccessTypeEntity.create()
				.withType(TYPE)
				.withAccess(List.of(AccessEntity.create()
					.withPattern("pattern")
					.withAccessLevel(AccessLevel.LR.name())))));

		when(accessUserRepositoryMock.findAllByMunicipalityIdAndNamespace(MUNICIPALITY_ID, NAMESPACE))
			.thenReturn(List.of(entity));

		// Act
		final var response = service.getAccessUsers(MUNICIPALITY_ID, NAMESPACE);

		// Assert
		assertThat(response).hasSize(1);
		assertThat(response.getFirst().getId()).isEqualTo(ID);
		assertThat(response.getFirst().getUserId()).isEqualTo(USER_ID);
		assertThat(response.getFirst().getAccessByType()).hasSize(1);

		verify(accessUserRepositoryMock).findAllByMunicipalityIdAndNamespace(MUNICIPALITY_ID, NAMESPACE);
	}

	@Test
	void getAccessUsersEmpty() {
		// Arrange
		when(accessUserRepositoryMock.findAllByMunicipalityIdAndNamespace(MUNICIPALITY_ID, NAMESPACE))
			.thenReturn(List.of());

		// Act
		final var response = service.getAccessUsers(MUNICIPALITY_ID, NAMESPACE);

		// Assert
		assertThat(response).isEmpty();

		verify(accessUserRepositoryMock).findAllByMunicipalityIdAndNamespace(MUNICIPALITY_ID, NAMESPACE);
	}

	@Test
	void getAccessUser() {
		// Arrange
		final var entity = AccessUserEntity.create()
			.withId(ID)
			.withMunicipalityId(MUNICIPALITY_ID)
			.withNamespace(NAMESPACE)
			.withUserId(USER_ID)
			.withAccessByType(List.of(AccessTypeEntity.create()
				.withType(TYPE)
				.withAccess(List.of(AccessEntity.create()
					.withPattern("pattern")
					.withAccessLevel(AccessLevel.LR.name())))));

		when(accessUserRepositoryMock.findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, ID))
			.thenReturn(Optional.of(entity));

		// Act
		final var response = service.getAccessUser(MUNICIPALITY_ID, NAMESPACE, ID);

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getId()).isEqualTo(ID);
		assertThat(response.getUserId()).isEqualTo(USER_ID);
		assertThat(response.getAccessByType()).hasSize(1);

		verify(accessUserRepositoryMock).findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, ID);
	}

	@Test
	void getAccessUserNotFound() {
		// Arrange
		when(accessUserRepositoryMock.findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, ID))
			.thenReturn(Optional.empty());

		// Act
		final var exception = assertThrows(ThrowableProblem.class,
			() -> service.getAccessUser(MUNICIPALITY_ID, NAMESPACE, ID));

		// Assert
		assertThat(exception.getStatus()).isEqualTo(NOT_FOUND);
		assertThat(exception.getTitle()).isEqualTo(NOT_FOUND.getReasonPhrase());
		assertThat(exception.getMessage()).isEqualTo("Not Found: Access user not found for municipalityId: municipalityId, namespace: namespace, id: id.");

		verify(accessUserRepositoryMock).findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, ID);
	}

	@Test
	void createAccessUser() {
		// Arrange
		final var accessUser = AccessUser.create()
			.withUserId(USER_ID)
			.withAccessByType(List.of(AccessType.create()
				.withType(TYPE)
				.withAccess(List.of(Access.create()
					.withPattern("pattern")
					.withAccessLevel(AccessLevel.RW)))));

		final var savedEntity = AccessUserEntity.create()
			.withId(ID)
			.withMunicipalityId(MUNICIPALITY_ID)
			.withNamespace(NAMESPACE)
			.withUserId(USER_ID)
			.withAccessByType(List.of(AccessTypeEntity.create()
				.withType(TYPE)
				.withAccess(List.of(AccessEntity.create()
					.withPattern("pattern")
					.withAccessLevel(AccessLevel.RW.name())))));

		when(accessUserRepositoryMock.save(any(AccessUserEntity.class))).thenReturn(savedEntity);

		// Act
		final var response = service.createAccessUser(MUNICIPALITY_ID, NAMESPACE, accessUser);

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getId()).isEqualTo(ID);
		assertThat(response.getUserId()).isEqualTo(USER_ID);
		assertThat(response.getAccessByType()).hasSize(1);

		verify(accessUserRepositoryMock).save(any(AccessUserEntity.class));
	}

	@Test
	void updateAccessUser() {
		// Arrange
		final var accessUser = AccessUser.create()
			.withUserId("newUserId")
			.withAccessByType(List.of(AccessType.create()
				.withType(TYPE)
				.withAccess(List.of(Access.create()
					.withPattern("newPattern")
					.withAccessLevel(AccessLevel.RW)))));

		final var existingEntity = AccessUserEntity.create()
			.withId(ID)
			.withMunicipalityId(MUNICIPALITY_ID)
			.withNamespace(NAMESPACE)
			.withUserId(USER_ID)
			.withAccessByType(new java.util.ArrayList<>(List.of(AccessTypeEntity.create()
				.withType(TYPE)
				.withAccess(List.of(AccessEntity.create()
					.withPattern("pattern")
					.withAccessLevel(AccessLevel.LR.name()))))));

		when(accessUserRepositoryMock.findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, ID))
			.thenReturn(Optional.of(existingEntity));
		when(accessUserRepositoryMock.save(any(AccessUserEntity.class))).thenReturn(existingEntity);

		// Act
		service.updateAccessUser(MUNICIPALITY_ID, NAMESPACE, ID, accessUser);

		// Assert
		verify(accessUserRepositoryMock).findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, ID);
		verify(accessUserRepositoryMock).save(any(AccessUserEntity.class));
	}

	@Test
	void updateAccessUserNotFound() {
		// Arrange
		final var accessUser = AccessUser.create()
			.withUserId(USER_ID);

		when(accessUserRepositoryMock.findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, ID))
			.thenReturn(Optional.empty());

		// Act
		final var exception = assertThrows(ThrowableProblem.class,
			() -> service.updateAccessUser(MUNICIPALITY_ID, NAMESPACE, ID, accessUser));

		// Assert
		assertThat(exception.getStatus()).isEqualTo(NOT_FOUND);
		assertThat(exception.getMessage()).isEqualTo("Not Found: Access user not found for municipalityId: municipalityId, namespace: namespace, id: id.");

		verify(accessUserRepositoryMock).findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, ID);
	}

	@Test
	void deleteAccessUser() {
		// Arrange
		final var entity = AccessUserEntity.create()
			.withId(ID)
			.withMunicipalityId(MUNICIPALITY_ID)
			.withNamespace(NAMESPACE)
			.withUserId(USER_ID);

		when(accessUserRepositoryMock.findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, ID))
			.thenReturn(Optional.of(entity));

		// Act
		service.deleteAccessUser(MUNICIPALITY_ID, NAMESPACE, ID);

		// Assert
		verify(accessUserRepositoryMock).findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, ID);
		verify(accessUserRepositoryMock).delete(entity);
	}

	@Test
	void deleteAccessUserNotFound() {
		// Arrange
		when(accessUserRepositoryMock.findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, ID))
			.thenReturn(Optional.empty());

		// Act
		final var exception = assertThrows(ThrowableProblem.class,
			() -> service.deleteAccessUser(MUNICIPALITY_ID, NAMESPACE, ID));

		// Assert
		assertThat(exception.getStatus()).isEqualTo(NOT_FOUND);
		assertThat(exception.getMessage()).isEqualTo("Not Found: Access user not found for municipalityId: municipalityId, namespace: namespace, id: id.");

		verify(accessUserRepositoryMock).findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, ID);
	}

	@AfterEach
	void verifyNoMoreInteractionsOnMocks() {
		verifyNoMoreInteractions(accessUserRepositoryMock);
	}
}

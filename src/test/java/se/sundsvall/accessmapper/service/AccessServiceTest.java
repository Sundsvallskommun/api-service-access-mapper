package se.sundsvall.accessmapper.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import generated.se.sundsvall.activedirectory.OUChildren;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sundsvall.accessmapper.api.model.AccessLevel;
import se.sundsvall.accessmapper.integration.activedirectory.ActiveDirectoryClient;
import se.sundsvall.accessmapper.integration.db.AccessGroupRepository;
import se.sundsvall.accessmapper.integration.db.model.AccessEntity;
import se.sundsvall.accessmapper.integration.db.model.AccessGroupEntity;
import se.sundsvall.accessmapper.integration.db.model.AccessTypeEntity;

@ExtendWith(MockitoExtension.class)
class AccessServiceTest {

	private static final String DOMAIN = "personal";

	private static final String NAMESPACE = "namespace";

	private static final String MUNICIPALITY_ID = "municipalityId";

	private static final String AD_ID = "adId";

	private static final String TYPE = "type";

	@Mock
	private ActiveDirectoryClient activeDirectoryClientMock;

	@Mock
	private AccessGroupRepository accessGroupRepositoryMock;

	@InjectMocks
	private AccessService service;

	@Test
	void getAccessDetailsWithTypeFilter() {
		// Arrange
		final var guid1 = UUID.randomUUID();
		final var guid2 = UUID.randomUUID();
		final var ouChild1 = new OUChildren().guid(guid1);
		final var ouChild2 = new OUChildren().guid(guid2);

		final var entity1 = AccessGroupEntity.create()
			.withId(guid1.toString())
			.withMunicipalityId(MUNICIPALITY_ID)
			.withNamespace(NAMESPACE)
			.withAccessByType(List.of(AccessTypeEntity
				.create()
				.withType(TYPE)
				.withAccess(List.of(AccessEntity
					.create()
					.withPattern("pattern1")
					.withAccessLevel(AccessLevel.LR.name())))));

		final var entity2 = AccessGroupEntity.create()
			.withId(guid2.toString())
			.withMunicipalityId(MUNICIPALITY_ID)
			.withNamespace(NAMESPACE)
			.withAccessByType(List.of(AccessTypeEntity
				.create()
				.withType(TYPE)
				.withAccess(List.of(AccessEntity
					.create()
					.withPattern("pattern2")
					.withAccessLevel(AccessLevel.RW.name())))));

		when(activeDirectoryClientMock.getGroupsForUser(MUNICIPALITY_ID, DOMAIN, AD_ID))
			.thenReturn(List.of(ouChild1, ouChild2));
		when(accessGroupRepositoryMock.findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, guid1.toString()))
			.thenReturn(entity1);
		when(accessGroupRepositoryMock.findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, guid2.toString()))
			.thenReturn(entity2);

		// Act
		final var response = service.getAccessDetails(MUNICIPALITY_ID, NAMESPACE, AD_ID, TYPE);

		// Assert
		assertThat(response).isNotNull().hasSize(2);
		assertThat(response.getFirst().getGroup()).isEqualTo(guid1.toString());
		assertThat(response.getLast().getGroup()).isEqualTo(guid2.toString());

		verify(activeDirectoryClientMock).getGroupsForUser(MUNICIPALITY_ID, DOMAIN, AD_ID);
		verify(accessGroupRepositoryMock).findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, guid1.toString());
		verify(accessGroupRepositoryMock).findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, guid2.toString());
	}

	@Test
	void getAccessDetailsWithoutTypeFilter() {
		// Arrange
		final var guid = UUID.randomUUID();
		final var ouChild = new OUChildren().guid(guid);

		final var entity = AccessGroupEntity.create()
			.withId(guid.toString())
			.withMunicipalityId(MUNICIPALITY_ID)
			.withNamespace(NAMESPACE)
			.withAccessByType(List.of(AccessTypeEntity
				.create()
				.withType(TYPE)
				.withAccess(List.of(AccessEntity
					.create()
					.withPattern("pattern")
					.withAccessLevel(AccessLevel.LR.name())))));

		when(activeDirectoryClientMock.getGroupsForUser(MUNICIPALITY_ID, DOMAIN, AD_ID))
			.thenReturn(List.of(ouChild));
		when(accessGroupRepositoryMock.findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, guid.toString()))
			.thenReturn(entity);

		// Act
		final var response = service.getAccessDetails(MUNICIPALITY_ID, NAMESPACE, AD_ID, null);

		// Assert
		assertThat(response).isNotNull().hasSize(1);
		assertThat(response.getFirst().getGroup()).isEqualTo(guid.toString());

		verify(activeDirectoryClientMock).getGroupsForUser(MUNICIPALITY_ID, DOMAIN, AD_ID);
		verify(accessGroupRepositoryMock).findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, guid.toString());
	}

	@Test
	void getAccessDetailsFilteredByType() {
		// Arrange
		final var guid1 = UUID.randomUUID();
		final var guid2 = UUID.randomUUID();
		final var ouChild1 = new OUChildren().guid(guid1);
		final var ouChild2 = new OUChildren().guid(guid2);

		final var entity1 = AccessGroupEntity.create()
			.withId(guid1.toString())
			.withMunicipalityId(MUNICIPALITY_ID)
			.withNamespace(NAMESPACE)
			.withAccessByType(List.of(AccessTypeEntity
				.create()
				.withType(TYPE)
				.withAccess(List.of(AccessEntity
					.create()
					.withPattern("pattern1")
					.withAccessLevel(AccessLevel.LR.name())))));

		final var entity2 = AccessGroupEntity.create()
			.withId(guid2.toString())
			.withMunicipalityId(MUNICIPALITY_ID)
			.withNamespace(NAMESPACE)
			.withAccessByType(List.of(AccessTypeEntity
				.create()
				.withType("otherType")
				.withAccess(List.of(AccessEntity
					.create()
					.withPattern("pattern2")
					.withAccessLevel(AccessLevel.RW.name())))));

		when(activeDirectoryClientMock.getGroupsForUser(MUNICIPALITY_ID, DOMAIN, AD_ID))
			.thenReturn(List.of(ouChild1, ouChild2));
		when(accessGroupRepositoryMock.findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, guid1.toString()))
			.thenReturn(entity1);
		when(accessGroupRepositoryMock.findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, guid2.toString()))
			.thenReturn(entity2);

		// Act
		final var response = service.getAccessDetails(MUNICIPALITY_ID, NAMESPACE, AD_ID, TYPE);

		// Assert
		assertThat(response).isNotNull().hasSize(1);
		assertThat(response.getFirst().getGroup()).isEqualTo(guid1.toString());

		verify(activeDirectoryClientMock).getGroupsForUser(MUNICIPALITY_ID, DOMAIN, AD_ID);
		verify(accessGroupRepositoryMock).findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, guid1.toString());
		verify(accessGroupRepositoryMock).findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, guid2.toString());
	}

	@Test
	void getAccessDetailsNoGroups() {
		// Arrange
		when(activeDirectoryClientMock.getGroupsForUser(MUNICIPALITY_ID, DOMAIN, AD_ID))
			.thenReturn(List.of());

		// Act
		final var response = service.getAccessDetails(MUNICIPALITY_ID, NAMESPACE, AD_ID, TYPE);

		// Assert
		assertThat(response).isNotNull().isEmpty();

		verify(activeDirectoryClientMock).getGroupsForUser(MUNICIPALITY_ID, DOMAIN, AD_ID);
	}

	@AfterEach
	void verifyNoMoreInteractionsOnMocks() {
		verifyNoMoreInteractions(activeDirectoryClientMock, accessGroupRepositoryMock);
	}
}

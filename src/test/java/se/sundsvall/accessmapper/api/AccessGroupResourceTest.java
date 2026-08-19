package se.sundsvall.accessmapper.api;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import se.sundsvall.accessmapper.Application;
import se.sundsvall.accessmapper.api.model.AccessGroup;
import se.sundsvall.accessmapper.service.AccessGroupService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.ALL;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
@AutoConfigureWebTestClient
class AccessGroupResourceTest {

	private static final String NAMESPACE = "namespace";

	private static final String MUNICIPALITY_ID = "2281";

	private static final String ID = "550e8400-e29b-41d4-a716-446655440000";

	private static final String PATH = "/{municipalityId}/{namespace}/access-config/group";

	@MockitoBean
	private AccessGroupService accessGroupServiceMock;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void getAccessGroups() {
		// Arrange
		final var accessGroups = List.of(new AccessGroup());

		when(accessGroupServiceMock.getAccessGroups(MUNICIPALITY_ID, NAMESPACE, null)).thenReturn(accessGroups);

		final var response = webTestClient.get().uri(builder -> builder.path(PATH)
			.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE)))
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBodyList(AccessGroup.class)
			.returnResult();

		assertThat(response.getResponseBody()).isEqualTo(accessGroups);

		// Assert
		verify(accessGroupServiceMock).getAccessGroups(MUNICIPALITY_ID, NAMESPACE, null);
	}

	@Test
	void getAccessGroupsWithType() {
		// Arrange
		final var type = "label";
		final var accessGroups = List.of(new AccessGroup());

		when(accessGroupServiceMock.getAccessGroups(MUNICIPALITY_ID, NAMESPACE, type)).thenReturn(accessGroups);

		final var response = webTestClient.get().uri(builder -> builder.path(PATH)
			.queryParam("type", type)
			.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE)))
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBodyList(AccessGroup.class)
			.returnResult();

		assertThat(response.getResponseBody()).isEqualTo(accessGroups);

		// Assert
		verify(accessGroupServiceMock).getAccessGroups(MUNICIPALITY_ID, NAMESPACE, type);
	}

	@Test
	void getAccessGroup() {
		// Arrange
		final var accessGroup = new AccessGroup();

		when(accessGroupServiceMock.getAccessGroup(MUNICIPALITY_ID, NAMESPACE, ID)).thenReturn(accessGroup);

		final var response = webTestClient.get().uri(builder -> builder.path(PATH.concat("/{id}"))
			.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE, "id", ID)))
			.accept(APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(AccessGroup.class)
			.returnResult();

		assertThat(response.getResponseBody()).isEqualTo(accessGroup);

		// Assert
		verify(accessGroupServiceMock).getAccessGroup(MUNICIPALITY_ID, NAMESPACE, ID);
	}

	@Test
	void createAccessGroup() {
		// Arrange
		final var accessGroup = new AccessGroup();

		// Act
		webTestClient.post().uri(builder -> builder.path(PATH)
			.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE)))
			.contentType(APPLICATION_JSON)
			.accept(ALL)
			.bodyValue(accessGroup)
			.exchange()
			.expectStatus().isCreated()
			.expectHeader().contentType(ALL)
			.expectHeader().location("/" + MUNICIPALITY_ID + "/" + NAMESPACE + "/access-config/group")
			.expectBody().isEmpty();

		// Assert
		verify(accessGroupServiceMock).createAccessGroup(eq(MUNICIPALITY_ID), eq(NAMESPACE), any(AccessGroup.class));
	}

	@Test
	void updateAccessGroup() {
		// Arrange
		final var accessGroup = new AccessGroup();

		// Act
		webTestClient.put().uri(builder -> builder.path(PATH.concat("/{id}"))
			.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE, "id", ID)))
			.contentType(APPLICATION_JSON)
			.accept(ALL)
			.bodyValue(accessGroup)
			.exchange()
			.expectStatus().isNoContent()
			.expectHeader().contentType(ALL_VALUE)
			.expectBody().isEmpty();

		// Assert
		verify(accessGroupServiceMock).updateAccessGroup(eq(MUNICIPALITY_ID), eq(NAMESPACE), eq(ID), any(AccessGroup.class));
	}

	@Test
	void deleteAccessGroup() {
		webTestClient.delete().uri(builder -> builder.path(PATH.concat("/{id}"))
			.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE, "id", ID)))
			.exchange()
			.expectStatus().isNoContent()
			.expectHeader().contentType(ALL_VALUE);

		// Assert
		verify(accessGroupServiceMock).deleteAccessGroup(MUNICIPALITY_ID, NAMESPACE, ID);
	}
}

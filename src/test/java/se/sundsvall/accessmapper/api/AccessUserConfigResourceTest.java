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
import se.sundsvall.accessmapper.api.model.AccessUser;
import se.sundsvall.accessmapper.service.AccessUserService;

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
class AccessUserConfigResourceTest {

	private static final String NAMESPACE = "namespace";

	private static final String MUNICIPALITY_ID = "2281";

	private static final String ID = "81471222-5798-11e9-ae24-57fa13b361e1";

	private static final String PATH = "/{municipalityId}/{namespace}/access-config/user";

	@MockitoBean
	private AccessUserService accessUserServiceMock;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void getAccessUsers() {
		// Arrange
		final var accessUsers = List.of(new AccessUser());

		when(accessUserServiceMock.getAccessUsers(MUNICIPALITY_ID, NAMESPACE)).thenReturn(accessUsers);

		final var response = webTestClient.get().uri(builder -> builder.path(PATH)
			.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE)))
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBodyList(AccessUser.class)
			.returnResult();

		assertThat(response.getResponseBody()).isEqualTo(accessUsers);

		// Assert
		verify(accessUserServiceMock).getAccessUsers(MUNICIPALITY_ID, NAMESPACE);
	}

	@Test
	void getAccessUser() {
		// Arrange
		final var accessUser = new AccessUser();

		when(accessUserServiceMock.getAccessUser(MUNICIPALITY_ID, NAMESPACE, ID)).thenReturn(accessUser);

		final var response = webTestClient.get().uri(builder -> builder.path(PATH.concat("/{id}"))
			.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE, "id", ID)))
			.accept(APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(AccessUser.class)
			.returnResult();

		assertThat(response.getResponseBody()).isEqualTo(accessUser);

		// Assert
		verify(accessUserServiceMock).getAccessUser(MUNICIPALITY_ID, NAMESPACE, ID);
	}

	@Test
	void createAccessUser() {
		// Arrange
		final var accessUser = AccessUser.create()
			.withUserId("joe01doe");

		final var createdUser = AccessUser.create()
			.withId(ID)
			.withUserId("joe01doe");

		when(accessUserServiceMock.createAccessUser(eq(MUNICIPALITY_ID), eq(NAMESPACE), any(AccessUser.class)))
			.thenReturn(createdUser);

		// Act
		webTestClient.post().uri(builder -> builder.path(PATH)
			.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE)))
			.contentType(APPLICATION_JSON)
			.accept(ALL)
			.bodyValue(accessUser)
			.exchange()
			.expectStatus().isCreated()
			.expectHeader().contentType(ALL)
			.expectHeader().location("/" + MUNICIPALITY_ID + "/" + NAMESPACE + "/access-config/user/" + ID)
			.expectBody().isEmpty();

		// Assert
		verify(accessUserServiceMock).createAccessUser(eq(MUNICIPALITY_ID), eq(NAMESPACE), any(AccessUser.class));
	}

	@Test
	void updateAccessUser() {
		// Arrange
		final var accessUser = AccessUser.create()
			.withUserId("joe01doe");

		// Act
		webTestClient.put().uri(builder -> builder.path(PATH.concat("/{id}"))
			.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE, "id", ID)))
			.contentType(APPLICATION_JSON)
			.accept(ALL)
			.bodyValue(accessUser)
			.exchange()
			.expectStatus().isNoContent()
			.expectHeader().contentType(ALL_VALUE)
			.expectBody().isEmpty();

		// Assert
		verify(accessUserServiceMock).updateAccessUser(eq(MUNICIPALITY_ID), eq(NAMESPACE), eq(ID), any(AccessUser.class));
	}

	@Test
	void deleteAccessUser() {
		webTestClient.delete().uri(builder -> builder.path(PATH.concat("/{id}"))
			.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE, "id", ID)))
			.exchange()
			.expectStatus().isNoContent()
			.expectHeader().contentType(ALL_VALUE);

		// Assert
		verify(accessUserServiceMock).deleteAccessUser(MUNICIPALITY_ID, NAMESPACE, ID);
	}
}

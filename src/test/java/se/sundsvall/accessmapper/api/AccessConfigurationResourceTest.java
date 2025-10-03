package se.sundsvall.accessmapper.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.ALL;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import se.sundsvall.accessmapper.Application;
import se.sundsvall.accessmapper.api.model.AccessGroup;
import se.sundsvall.accessmapper.service.AccessConfigurationService;

@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
class AccessConfigurationResourceTest {

	private static final String NAMESPACE = "namespace";

	private static final String MUNICIPALITY_ID = "2281";

	private static final String GROUP_ID = "group123";

	private static final String PATH = "/{municipalityId}/{namespace}/access-config/group";

	@MockitoBean
	private AccessConfigurationService accessConfigurationServiceMock;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void getAccessConfigurations() {
		// Parameter values
		final var accessGroups = List.of(new AccessGroup());

		when(accessConfigurationServiceMock.getAccessConfigurations(MUNICIPALITY_ID, NAMESPACE, null)).thenReturn(accessGroups);

		final var response = webTestClient.get().uri(builder -> builder.path(PATH)
			.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE)))
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBodyList(AccessGroup.class)
			.returnResult();

		assertThat(response.getResponseBody()).isEqualTo(accessGroups);

		// Verification
		verify(accessConfigurationServiceMock).getAccessConfigurations(MUNICIPALITY_ID, NAMESPACE, null);
	}

	@Test
	void getAccessConfigurationsWithType() {
		// Parameter values
		final var type = "label";
		final var accessGroups = List.of(new AccessGroup());

		when(accessConfigurationServiceMock.getAccessConfigurations(MUNICIPALITY_ID, NAMESPACE, type)).thenReturn(accessGroups);

		final var response = webTestClient.get().uri(builder -> builder.path(PATH)
			.queryParam("type", type)
			.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE)))
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBodyList(AccessGroup.class)
			.returnResult();

		assertThat(response.getResponseBody()).isEqualTo(accessGroups);

		// Verification
		verify(accessConfigurationServiceMock).getAccessConfigurations(MUNICIPALITY_ID, NAMESPACE, type);
	}

	@Test
	void getAccessConfiguration() {
		// Parameter values
		final var accessGroup = new AccessGroup();

		when(accessConfigurationServiceMock.getAccessConfiguration(MUNICIPALITY_ID, NAMESPACE, GROUP_ID)).thenReturn(accessGroup);

		final var response = webTestClient.get().uri(builder -> builder.path(PATH.concat("/{groupId}"))
			.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE, "groupId", GROUP_ID)))
			.accept(APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(AccessGroup.class)
			.returnResult();

		assertThat(response.getResponseBody()).isEqualTo(accessGroup);

		// Verification
		verify(accessConfigurationServiceMock).getAccessConfiguration(MUNICIPALITY_ID, NAMESPACE, GROUP_ID);
	}

	@Test
	void createAccessConfiguration() {
		// Parameter values
		final var accessGroup = new AccessGroup();

		// Call
		webTestClient.post().uri(builder -> builder.path(PATH.concat("/{groupId}"))
			.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE, "groupId", GROUP_ID)))
			.contentType(APPLICATION_JSON)
			.accept(ALL)
			.bodyValue(accessGroup)
			.exchange()
			.expectStatus().isCreated()
			.expectHeader().contentType(ALL)
			.expectHeader().location("/" + MUNICIPALITY_ID + "/" + NAMESPACE + "/access-config/group/" + GROUP_ID)
			.expectBody().isEmpty();

		// Verification
		verify(accessConfigurationServiceMock).createAccessConfiguration(eq(MUNICIPALITY_ID), eq(NAMESPACE), eq(GROUP_ID), any(AccessGroup.class));
	}

	@Test
	void updateAccessConfiguration() {
		// Parameter values
		final var accessGroup = new AccessGroup();

		// Call
		webTestClient.put().uri(builder -> builder.path(PATH.concat("/{groupId}"))
			.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE, "groupId", GROUP_ID)))
			.contentType(APPLICATION_JSON)
			.accept(ALL)
			.bodyValue(accessGroup)
			.exchange()
			.expectStatus().isNoContent()
			.expectHeader().contentType(ALL_VALUE)
			.expectBody().isEmpty();

		// Verification
		verify(accessConfigurationServiceMock).updateAccessConfiguration(eq(MUNICIPALITY_ID), eq(NAMESPACE), eq(GROUP_ID), any(AccessGroup.class));
	}

	@Test
	void deleteAccessConfiguration() {
		webTestClient.delete().uri(builder -> builder.path(PATH.concat("/{groupId}"))
			.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE, "groupId", GROUP_ID)))
			.exchange()
			.expectStatus().isNoContent()
			.expectHeader().contentType(ALL_VALUE);

		// Verification
		verify(accessConfigurationServiceMock).deleteAccessConfiguration(MUNICIPALITY_ID, NAMESPACE, GROUP_ID);
	}
}

package se.sundsvall.accessmapper.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
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
import se.sundsvall.accessmapper.service.AccessService;

@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
class AccessResourceTest {

	private static final String NAMESPACE = "namespace";

	private static final String MUNICIPALITY_ID = "2281";

	private static final String AD_ID = "joe01doe";

	private static final String PATH = "/{municipalityId}/{namespace}/access/ad/{adId}";

	@MockitoBean
	private AccessService accessServiceMock;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void getAccessDetails() {
		// Parameter values
		final var accessGroups = List.of(new AccessGroup());

		when(accessServiceMock.getAccessDetails(MUNICIPALITY_ID, NAMESPACE, AD_ID, null)).thenReturn(accessGroups);

		final var response = webTestClient.get().uri(builder -> builder.path(PATH)
			.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE, "adId", AD_ID)))
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBodyList(AccessGroup.class)
			.returnResult();

		assertThat(response.getResponseBody()).isEqualTo(accessGroups);

		// Verification
		verify(accessServiceMock).getAccessDetails(MUNICIPALITY_ID, NAMESPACE, AD_ID, null);
	}

	@Test
	void getAccessDetailsWithType() {
		// Parameter values
		final var type = "label";
		final var accessGroups = List.of(new AccessGroup());

		when(accessServiceMock.getAccessDetails(MUNICIPALITY_ID, NAMESPACE, AD_ID, type)).thenReturn(accessGroups);

		final var response = webTestClient.get().uri(builder -> builder.path(PATH)
			.queryParam("type", type)
			.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE, "adId", AD_ID)))
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBodyList(AccessGroup.class)
			.returnResult();

		assertThat(response.getResponseBody()).isEqualTo(accessGroups);

		// Verification
		verify(accessServiceMock).getAccessDetails(MUNICIPALITY_ID, NAMESPACE, AD_ID, type);
	}
}

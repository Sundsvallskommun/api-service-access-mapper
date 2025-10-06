package se.sundsvall.accessmapper.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.zalando.problem.Status.BAD_REQUEST;
import static org.zalando.problem.Status.NOT_FOUND;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;
import org.zalando.problem.violations.Violation;
import se.sundsvall.accessmapper.Application;
import se.sundsvall.accessmapper.service.AccessService;

@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
class AccessResourceFailureTest {

	private static final String NAMESPACE = "namespace";
	private static final String MUNICIPALITY_ID = "2281";
	private static final String AD_ID = "joe01doe";
	private static final String INVALID = "#invalid#";

	private static final String PATH = "/{municipalityId}/{namespace}/access/ad/{adId}";

	@Autowired
	private WebTestClient webTestClient;

	@MockitoBean
	private AccessService accessServiceMock;

	@Test
	void getAccessDetailsWithInvalidNamespace() {

		// Call
		final var response = webTestClient.get()
			.uri(builder -> builder.path(PATH).build(Map.of("namespace", INVALID, "municipalityId", MUNICIPALITY_ID, "adId", AD_ID)))
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactlyInAnyOrder(tuple("getAccessDetails.namespace", "can only contain A-Z, a-z, 0-9, - and _"));

		// Verification
		verifyNoInteractions(accessServiceMock);
	}

	@Test
	void getAccessDetailsWithInvalidMunicipalityId() {

		// Call
		final var response = webTestClient.get()
			.uri(builder -> builder.path(PATH).build(Map.of("namespace", NAMESPACE, "municipalityId", INVALID, "adId", AD_ID)))
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactlyInAnyOrder(tuple("getAccessDetails.municipalityId", "not a valid municipality ID"));

		// Verification
		verifyNoInteractions(accessServiceMock);
	}

	@Test
	void getAccessDetailsNotFound() {

		// Arrange
		when(accessServiceMock.getAccessDetails(MUNICIPALITY_ID, NAMESPACE, AD_ID, null))
			.thenThrow(Problem.valueOf(NOT_FOUND, "Access details not found for adId: " + AD_ID));

		// Call
		final var response = webTestClient.get()
			.uri(builder -> builder.path(PATH).build(Map.of("namespace", NAMESPACE, "municipalityId", MUNICIPALITY_ID, "adId", AD_ID)))
			.exchange()
			.expectStatus().isNotFound()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody(Problem.class)
			.returnResult()
			.getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getStatus()).isEqualTo(NOT_FOUND);
		assertThat(response.getDetail()).isEqualTo("Access details not found for adId: " + AD_ID);
	}
}

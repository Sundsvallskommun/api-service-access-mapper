package se.sundsvall.accessmapper.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.zalando.problem.Status.BAD_REQUEST;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.zalando.problem.violations.ConstraintViolationProblem;
import org.zalando.problem.violations.Violation;
import se.sundsvall.accessmapper.Application;
import se.sundsvall.accessmapper.api.model.AccessGroup;
import se.sundsvall.accessmapper.service.AccessConfigurationService;

@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
class AccessConfigurationResourceFailureTest {

	private static final String NAMESPACE = "namespace";
	private static final String MUNICIPALITY_ID = "2281";
	private static final String GROUP_ID = "group123";
	private static final String INVALID = "#invalid#";

	private static final String PATH = "/{municipalityId}/{namespace}/access-config/group";

	@Autowired
	private WebTestClient webTestClient;

	@MockitoBean
	private AccessConfigurationService accessConfigurationServiceMock;

	@Test
	void getAccessConfigurationsWithInvalidNamespace() {

		// Call
		final var response = webTestClient.get()
			.uri(builder -> builder.path(PATH).build(Map.of("namespace", INVALID, "municipalityId", MUNICIPALITY_ID)))
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
			.containsExactlyInAnyOrder(tuple("getAccessConfigurations.namespace", "can only contain A-Z, a-z, 0-9, - and _"));

		// Verification
		verifyNoInteractions(accessConfigurationServiceMock);
	}

	@Test
	void getAccessConfigurationsWithInvalidMunicipalityId() {

		// Call
		final var response = webTestClient.get()
			.uri(builder -> builder.path(PATH).build(Map.of("namespace", NAMESPACE, "municipalityId", INVALID)))
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
			.containsExactlyInAnyOrder(tuple("getAccessConfigurations.municipalityId", "not a valid municipality ID"));

		// Verification
		verifyNoInteractions(accessConfigurationServiceMock);
	}

	@Test
	void getAccessConfigurationWithInvalidNamespace() {

		// Call
		final var response = webTestClient.get()
			.uri(builder -> builder.path(PATH.concat("/{groupId}")).build(Map.of("namespace", INVALID, "municipalityId", MUNICIPALITY_ID, "groupId", GROUP_ID)))
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
			.containsExactlyInAnyOrder(tuple("getAccessConfiguration.namespace", "can only contain A-Z, a-z, 0-9, - and _"));

		// Verification
		verifyNoInteractions(accessConfigurationServiceMock);
	}

	@Test
	void getAccessConfigurationWithInvalidMunicipalityId() {

		// Call
		final var response = webTestClient.get()
			.uri(builder -> builder.path(PATH.concat("/{groupId}")).build(Map.of("namespace", NAMESPACE, "municipalityId", INVALID, "groupId", GROUP_ID)))
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
			.containsExactlyInAnyOrder(tuple("getAccessConfiguration.municipalityId", "not a valid municipality ID"));

		// Verification
		verifyNoInteractions(accessConfigurationServiceMock);
	}

	@Test
	void getAccessConfigurationWithBlankGroupId() {

		// Call
		final var response = webTestClient.get()
			.uri(builder -> builder.path(PATH.concat("/{groupId}")).build(Map.of("namespace", NAMESPACE, "municipalityId", MUNICIPALITY_ID, "groupId", " ")))
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
			.containsExactlyInAnyOrder(tuple("getAccessConfiguration.groupId", "must not be blank"));

		// Verification
		verifyNoInteractions(accessConfigurationServiceMock);
	}

	@Test
	void createAccessConfigurationWithInvalidNamespace() {

		// Parameters
		final var accessGroup = new AccessGroup();

		// Call
		final var response = webTestClient.post()
			.uri(builder -> builder.path(PATH.concat("/{groupId}")).build(Map.of("namespace", INVALID, "municipalityId", MUNICIPALITY_ID, "groupId", GROUP_ID)))
			.contentType(APPLICATION_JSON)
			.bodyValue(accessGroup)
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
			.containsExactlyInAnyOrder(tuple("createAccessConfiguration.namespace", "can only contain A-Z, a-z, 0-9, - and _"));

		// Verification
		verifyNoInteractions(accessConfigurationServiceMock);
	}

	@Test
	void createAccessConfigurationWithInvalidMunicipalityId() {

		// Parameters
		final var accessGroup = new AccessGroup();

		// Call
		final var response = webTestClient.post()
			.uri(builder -> builder.path(PATH.concat("/{groupId}")).build(Map.of("namespace", NAMESPACE, "municipalityId", INVALID, "groupId", GROUP_ID)))
			.contentType(APPLICATION_JSON)
			.bodyValue(accessGroup)
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
			.containsExactlyInAnyOrder(tuple("createAccessConfiguration.municipalityId", "not a valid municipality ID"));

		// Verification
		verifyNoInteractions(accessConfigurationServiceMock);
	}

	@Test
	void createAccessConfigurationWithBlankGroupId() {

		// Parameters
		final var accessGroup = new AccessGroup();

		// Call
		final var response = webTestClient.post()
			.uri(builder -> builder.path(PATH.concat("/{groupId}")).build(Map.of("namespace", NAMESPACE, "municipalityId", MUNICIPALITY_ID, "groupId", " ")))
			.contentType(APPLICATION_JSON)
			.bodyValue(accessGroup)
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
			.containsExactlyInAnyOrder(tuple("createAccessConfiguration.groupId", "must not be blank"));

		// Verification
		verifyNoInteractions(accessConfigurationServiceMock);
	}

	@Test
	void updateAccessConfigurationWithInvalidNamespace() {

		// Parameters
		final var accessGroup = new AccessGroup();

		// Call
		final var response = webTestClient.put()
			.uri(builder -> builder.path(PATH.concat("/{groupId}")).build(Map.of("namespace", INVALID, "municipalityId", MUNICIPALITY_ID, "groupId", GROUP_ID)))
			.contentType(APPLICATION_JSON)
			.bodyValue(accessGroup)
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
			.containsExactlyInAnyOrder(tuple("updateAccessConfiguration.namespace", "can only contain A-Z, a-z, 0-9, - and _"));

		// Verification
		verifyNoInteractions(accessConfigurationServiceMock);
	}

	@Test
	void updateAccessConfigurationWithInvalidMunicipalityId() {

		// Parameters
		final var accessGroup = new AccessGroup();

		// Call
		final var response = webTestClient.put()
			.uri(builder -> builder.path(PATH.concat("/{groupId}")).build(Map.of("namespace", NAMESPACE, "municipalityId", INVALID, "groupId", GROUP_ID)))
			.contentType(APPLICATION_JSON)
			.bodyValue(accessGroup)
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
			.containsExactlyInAnyOrder(tuple("updateAccessConfiguration.municipalityId", "not a valid municipality ID"));

		// Verification
		verifyNoInteractions(accessConfigurationServiceMock);
	}

	@Test
	void updateAccessConfigurationWithBlankGroupId() {

		// Parameters
		final var accessGroup = new AccessGroup();

		// Call
		final var response = webTestClient.put()
			.uri(builder -> builder.path(PATH.concat("/{groupId}")).build(Map.of("namespace", NAMESPACE, "municipalityId", MUNICIPALITY_ID, "groupId", " ")))
			.contentType(APPLICATION_JSON)
			.bodyValue(accessGroup)
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
			.containsExactlyInAnyOrder(tuple("updateAccessConfiguration.groupId", "must not be blank"));

		// Verification
		verifyNoInteractions(accessConfigurationServiceMock);
	}

	@Test
	void deleteAccessConfigurationWithInvalidNamespace() {

		// Call
		final var response = webTestClient.delete()
			.uri(builder -> builder.path(PATH.concat("/{groupId}")).build(Map.of("namespace", INVALID, "municipalityId", MUNICIPALITY_ID, "groupId", GROUP_ID)))
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
			.containsExactlyInAnyOrder(tuple("deleteAccessConfiguration.namespace", "can only contain A-Z, a-z, 0-9, - and _"));

		// Verification
		verifyNoInteractions(accessConfigurationServiceMock);
	}

	@Test
	void deleteAccessConfigurationWithInvalidMunicipalityId() {

		// Call
		final var response = webTestClient.delete()
			.uri(builder -> builder.path(PATH.concat("/{groupId}")).build(Map.of("namespace", NAMESPACE, "municipalityId", INVALID, "groupId", GROUP_ID)))
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
			.containsExactlyInAnyOrder(tuple("deleteAccessConfiguration.municipalityId", "not a valid municipality ID"));

		// Verification
		verifyNoInteractions(accessConfigurationServiceMock);
	}

	@Test
	void deleteAccessConfigurationWithBlankGroupId() {

		// Call
		final var response = webTestClient.delete()
			.uri(builder -> builder.path(PATH.concat("/{groupId}")).build(Map.of("namespace", NAMESPACE, "municipalityId", MUNICIPALITY_ID, "groupId", " ")))
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
			.containsExactlyInAnyOrder(tuple("deleteAccessConfiguration.groupId", "must not be blank"));

		// Verification
		verifyNoInteractions(accessConfigurationServiceMock);
	}

}

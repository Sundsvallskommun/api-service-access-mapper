package se.sundsvall.accessmapper.api;

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
import se.sundsvall.dept44.problem.violations.ConstraintViolationProblem;
import se.sundsvall.dept44.problem.violations.Violation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
@AutoConfigureWebTestClient
class AccessGroupResourceFailureTest {

	private static final String NAMESPACE = "namespace";
	private static final String MUNICIPALITY_ID = "2281";
	private static final String ID = "550e8400-e29b-41d4-a716-446655440000";
	private static final String INVALID = "#invalid#";

	private static final String PATH = "/{municipalityId}/{namespace}/access-config/group";

	@Autowired
	private WebTestClient webTestClient;

	@MockitoBean
	private AccessGroupService accessGroupServiceMock;

	@Test
	void getAccessGroupsWithInvalidNamespace() {

		// Act
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
			.extracting(Violation::field, Violation::message)
			.containsExactlyInAnyOrder(tuple("getAccessGroups.namespace", "can only contain A-Z, a-z, 0-9, - and _"));

		// Assert
		verifyNoInteractions(accessGroupServiceMock);
	}

	@Test
	void getAccessGroupsWithInvalidMunicipalityId() {

		// Act
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
			.extracting(Violation::field, Violation::message)
			.containsExactlyInAnyOrder(tuple("getAccessGroups.municipalityId", "not a valid municipality ID"));

		// Assert
		verifyNoInteractions(accessGroupServiceMock);
	}

	@Test
	void getAccessGroupWithInvalidNamespace() {

		// Act
		final var response = webTestClient.get()
			.uri(builder -> builder.path(PATH.concat("/{id}")).build(Map.of("namespace", INVALID, "municipalityId", MUNICIPALITY_ID, "id", ID)))
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::field, Violation::message)
			.containsExactlyInAnyOrder(tuple("getAccessGroup.namespace", "can only contain A-Z, a-z, 0-9, - and _"));

		// Assert
		verifyNoInteractions(accessGroupServiceMock);
	}

	@Test
	void getAccessGroupWithInvalidMunicipalityId() {

		// Act
		final var response = webTestClient.get()
			.uri(builder -> builder.path(PATH.concat("/{id}")).build(Map.of("namespace", NAMESPACE, "municipalityId", INVALID, "id", ID)))
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::field, Violation::message)
			.containsExactlyInAnyOrder(tuple("getAccessGroup.municipalityId", "not a valid municipality ID"));

		// Assert
		verifyNoInteractions(accessGroupServiceMock);
	}

	@Test
	void getAccessGroupWithInvalidId() {

		// Act
		final var response = webTestClient.get()
			.uri(builder -> builder.path(PATH.concat("/{id}")).build(Map.of("namespace", NAMESPACE, "municipalityId", MUNICIPALITY_ID, "id", "not-a-uuid")))
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::field, Violation::message)
			.containsExactlyInAnyOrder(tuple("getAccessGroup.id", "not a valid UUID"));

		// Assert
		verifyNoInteractions(accessGroupServiceMock);
	}

	@Test
	void createAccessGroupWithInvalidNamespace() {

		// Arrange
		final var accessGroup = new AccessGroup();

		// Act
		final var response = webTestClient.post()
			.uri(builder -> builder.path(PATH).build(Map.of("namespace", INVALID, "municipalityId", MUNICIPALITY_ID)))
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
			.extracting(Violation::field, Violation::message)
			.containsExactlyInAnyOrder(tuple("createAccessGroup.namespace", "can only contain A-Z, a-z, 0-9, - and _"));

		// Assert
		verifyNoInteractions(accessGroupServiceMock);
	}

	@Test
	void createAccessGroupWithInvalidMunicipalityId() {

		// Arrange
		final var accessGroup = new AccessGroup();

		// Act
		final var response = webTestClient.post()
			.uri(builder -> builder.path(PATH).build(Map.of("namespace", NAMESPACE, "municipalityId", INVALID)))
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
			.extracting(Violation::field, Violation::message)
			.containsExactlyInAnyOrder(tuple("createAccessGroup.municipalityId", "not a valid municipality ID"));

		// Assert
		verifyNoInteractions(accessGroupServiceMock);
	}

	@Test
	void updateAccessGroupWithInvalidNamespace() {

		// Arrange
		final var accessGroup = new AccessGroup();

		// Act
		final var response = webTestClient.put()
			.uri(builder -> builder.path(PATH.concat("/{id}")).build(Map.of("namespace", INVALID, "municipalityId", MUNICIPALITY_ID, "id", ID)))
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
			.extracting(Violation::field, Violation::message)
			.containsExactlyInAnyOrder(tuple("updateAccessGroup.namespace", "can only contain A-Z, a-z, 0-9, - and _"));

		// Assert
		verifyNoInteractions(accessGroupServiceMock);
	}

	@Test
	void updateAccessGroupWithInvalidMunicipalityId() {

		// Arrange
		final var accessGroup = new AccessGroup();

		// Act
		final var response = webTestClient.put()
			.uri(builder -> builder.path(PATH.concat("/{id}")).build(Map.of("namespace", NAMESPACE, "municipalityId", INVALID, "id", ID)))
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
			.extracting(Violation::field, Violation::message)
			.containsExactlyInAnyOrder(tuple("updateAccessGroup.municipalityId", "not a valid municipality ID"));

		// Assert
		verifyNoInteractions(accessGroupServiceMock);
	}

	@Test
	void updateAccessGroupWithInvalidId() {

		// Arrange
		final var accessGroup = new AccessGroup();

		// Act
		final var response = webTestClient.put()
			.uri(builder -> builder.path(PATH.concat("/{id}")).build(Map.of("namespace", NAMESPACE, "municipalityId", MUNICIPALITY_ID, "id", "not-a-uuid")))
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
			.extracting(Violation::field, Violation::message)
			.containsExactlyInAnyOrder(tuple("updateAccessGroup.id", "not a valid UUID"));

		// Assert
		verifyNoInteractions(accessGroupServiceMock);
	}

	@Test
	void deleteAccessGroupWithInvalidNamespace() {

		// Act
		final var response = webTestClient.delete()
			.uri(builder -> builder.path(PATH.concat("/{id}")).build(Map.of("namespace", INVALID, "municipalityId", MUNICIPALITY_ID, "id", ID)))
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::field, Violation::message)
			.containsExactlyInAnyOrder(tuple("deleteAccessGroup.namespace", "can only contain A-Z, a-z, 0-9, - and _"));

		// Assert
		verifyNoInteractions(accessGroupServiceMock);
	}

	@Test
	void deleteAccessGroupWithInvalidMunicipalityId() {

		// Act
		final var response = webTestClient.delete()
			.uri(builder -> builder.path(PATH.concat("/{id}")).build(Map.of("namespace", NAMESPACE, "municipalityId", INVALID, "id", ID)))
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::field, Violation::message)
			.containsExactlyInAnyOrder(tuple("deleteAccessGroup.municipalityId", "not a valid municipality ID"));

		// Assert
		verifyNoInteractions(accessGroupServiceMock);
	}

	@Test
	void deleteAccessGroupWithInvalidId() {

		// Act
		final var response = webTestClient.delete()
			.uri(builder -> builder.path(PATH.concat("/{id}")).build(Map.of("namespace", NAMESPACE, "municipalityId", MUNICIPALITY_ID, "id", "not-a-uuid")))
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::field, Violation::message)
			.containsExactlyInAnyOrder(tuple("deleteAccessGroup.id", "not a valid UUID"));

		// Assert
		verifyNoInteractions(accessGroupServiceMock);
	}

}

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
import se.sundsvall.accessmapper.api.model.AccessUser;
import se.sundsvall.accessmapper.service.AccessUserService;
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
class AccessUserConfigResourceFailureTest {

	private static final String NAMESPACE = "namespace";
	private static final String MUNICIPALITY_ID = "2281";
	private static final String ID = "81471222-5798-11e9-ae24-57fa13b361e1";
	private static final String INVALID = "invalid!";

	private static final String PATH = "/{municipalityId}/{namespace}/access-config/user";

	@Autowired
	private WebTestClient webTestClient;

	@MockitoBean
	private AccessUserService accessUserServiceMock;

	@Test
	void getAccessUsersWithInvalidNamespace() {

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
			.containsExactlyInAnyOrder(tuple("getAccessUsers.namespace", "can only contain A-Z, a-z, 0-9, - and _"));

		// Assert
		verifyNoInteractions(accessUserServiceMock);
	}

	@Test
	void getAccessUsersWithInvalidMunicipalityId() {

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
			.containsExactlyInAnyOrder(tuple("getAccessUsers.municipalityId", "not a valid municipality ID"));

		// Assert
		verifyNoInteractions(accessUserServiceMock);
	}

	@Test
	void getAccessUserWithInvalidNamespace() {

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
			.containsExactlyInAnyOrder(tuple("getAccessUser.namespace", "can only contain A-Z, a-z, 0-9, - and _"));

		// Assert
		verifyNoInteractions(accessUserServiceMock);
	}

	@Test
	void getAccessUserWithInvalidMunicipalityId() {

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
			.containsExactlyInAnyOrder(tuple("getAccessUser.municipalityId", "not a valid municipality ID"));

		// Assert
		verifyNoInteractions(accessUserServiceMock);
	}

	@Test
	void getAccessUserWithBlankId() {

		// Act
		final var response = webTestClient.get()
			.uri(builder -> builder.path(PATH.concat("/{id}")).build(Map.of("namespace", NAMESPACE, "municipalityId", MUNICIPALITY_ID, "id", " ")))
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
			.containsExactlyInAnyOrder(tuple("getAccessUser.id", "must not be blank"));

		// Assert
		verifyNoInteractions(accessUserServiceMock);
	}

	@Test
	void createAccessUserWithInvalidNamespace() {

		// Arrange
		final var accessUser = new AccessUser();

		// Act
		final var response = webTestClient.post()
			.uri(builder -> builder.path(PATH).build(Map.of("namespace", INVALID, "municipalityId", MUNICIPALITY_ID)))
			.contentType(APPLICATION_JSON)
			.bodyValue(accessUser)
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
			.containsExactlyInAnyOrder(tuple("createAccessUser.namespace", "can only contain A-Z, a-z, 0-9, - and _"));

		// Assert
		verifyNoInteractions(accessUserServiceMock);
	}

	@Test
	void createAccessUserWithInvalidMunicipalityId() {

		// Arrange
		final var accessUser = new AccessUser();

		// Act
		final var response = webTestClient.post()
			.uri(builder -> builder.path(PATH).build(Map.of("namespace", NAMESPACE, "municipalityId", INVALID)))
			.contentType(APPLICATION_JSON)
			.bodyValue(accessUser)
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
			.containsExactlyInAnyOrder(tuple("createAccessUser.municipalityId", "not a valid municipality ID"));

		// Assert
		verifyNoInteractions(accessUserServiceMock);
	}

	@Test
	void updateAccessUserWithInvalidNamespace() {

		// Arrange
		final var accessUser = new AccessUser();

		// Act
		final var response = webTestClient.put()
			.uri(builder -> builder.path(PATH.concat("/{id}")).build(Map.of("namespace", INVALID, "municipalityId", MUNICIPALITY_ID, "id", ID)))
			.contentType(APPLICATION_JSON)
			.bodyValue(accessUser)
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
			.containsExactlyInAnyOrder(tuple("updateAccessUser.namespace", "can only contain A-Z, a-z, 0-9, - and _"));

		// Assert
		verifyNoInteractions(accessUserServiceMock);
	}

	@Test
	void updateAccessUserWithInvalidMunicipalityId() {

		// Arrange
		final var accessUser = new AccessUser();

		// Act
		final var response = webTestClient.put()
			.uri(builder -> builder.path(PATH.concat("/{id}")).build(Map.of("namespace", NAMESPACE, "municipalityId", INVALID, "id", ID)))
			.contentType(APPLICATION_JSON)
			.bodyValue(accessUser)
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
			.containsExactlyInAnyOrder(tuple("updateAccessUser.municipalityId", "not a valid municipality ID"));

		// Assert
		verifyNoInteractions(accessUserServiceMock);
	}

	@Test
	void updateAccessUserWithBlankId() {

		// Arrange
		final var accessUser = new AccessUser();

		// Act
		final var response = webTestClient.put()
			.uri(builder -> builder.path(PATH.concat("/{id}")).build(Map.of("namespace", NAMESPACE, "municipalityId", MUNICIPALITY_ID, "id", " ")))
			.contentType(APPLICATION_JSON)
			.bodyValue(accessUser)
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
			.containsExactlyInAnyOrder(tuple("updateAccessUser.id", "must not be blank"));

		// Assert
		verifyNoInteractions(accessUserServiceMock);
	}

	@Test
	void deleteAccessUserWithInvalidNamespace() {

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
			.containsExactlyInAnyOrder(tuple("deleteAccessUser.namespace", "can only contain A-Z, a-z, 0-9, - and _"));

		// Assert
		verifyNoInteractions(accessUserServiceMock);
	}

	@Test
	void deleteAccessUserWithInvalidMunicipalityId() {

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
			.containsExactlyInAnyOrder(tuple("deleteAccessUser.municipalityId", "not a valid municipality ID"));

		// Assert
		verifyNoInteractions(accessUserServiceMock);
	}

	@Test
	void deleteAccessUserWithBlankId() {

		// Act
		final var response = webTestClient.delete()
			.uri(builder -> builder.path(PATH.concat("/{id}")).build(Map.of("namespace", NAMESPACE, "municipalityId", MUNICIPALITY_ID, "id", " ")))
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
			.containsExactlyInAnyOrder(tuple("deleteAccessUser.id", "must not be blank"));

		// Assert
		verifyNoInteractions(accessUserServiceMock);
	}
}

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
import se.sundsvall.accessmapper.service.AccessGroupService;

@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
class AccessGroupResourceFailureTest {

	private static final String NAMESPACE = "namespace";
	private static final String MUNICIPALITY_ID = "2281";
	private static final String GROUP_ID = "group123";
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
			.extracting(Violation::getField, Violation::getMessage)
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
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactlyInAnyOrder(tuple("getAccessGroups.municipalityId", "not a valid municipality ID"));

		// Assert
		verifyNoInteractions(accessGroupServiceMock);
	}

	@Test
	void getAccessGroupWithInvalidNamespace() {

		// Act
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
			.containsExactlyInAnyOrder(tuple("getAccessGroup.namespace", "can only contain A-Z, a-z, 0-9, - and _"));

		// Assert
		verifyNoInteractions(accessGroupServiceMock);
	}

	@Test
	void getAccessGroupWithInvalidMunicipalityId() {

		// Act
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
			.containsExactlyInAnyOrder(tuple("getAccessGroup.municipalityId", "not a valid municipality ID"));

		// Assert
		verifyNoInteractions(accessGroupServiceMock);
	}

	@Test
	void getAccessGroupWithBlankGroupId() {

		// Act
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
			.containsExactlyInAnyOrder(tuple("getAccessGroup.groupId", "must not be blank"));

		// Assert
		verifyNoInteractions(accessGroupServiceMock);
	}

	@Test
	void createAccessGroupWithInvalidNamespace() {

		// Arrange
		final var accessGroup = new AccessGroup();

		// Act
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
			.containsExactlyInAnyOrder(tuple("createAccessGroup.municipalityId", "not a valid municipality ID"));

		// Assert
		verifyNoInteractions(accessGroupServiceMock);
	}

	@Test
	void createAccessGroupWithBlankGroupId() {

		// Arrange
		final var accessGroup = new AccessGroup();

		// Act
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
			.containsExactlyInAnyOrder(tuple("createAccessGroup.groupId", "must not be blank"));

		// Assert
		verifyNoInteractions(accessGroupServiceMock);
	}

	@Test
	void updateAccessGroupWithInvalidNamespace() {

		// Arrange
		final var accessGroup = new AccessGroup();

		// Act
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
			.containsExactlyInAnyOrder(tuple("updateAccessGroup.municipalityId", "not a valid municipality ID"));

		// Assert
		verifyNoInteractions(accessGroupServiceMock);
	}

	@Test
	void updateAccessGroupWithBlankGroupId() {

		// Arrange
		final var accessGroup = new AccessGroup();

		// Act
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
			.containsExactlyInAnyOrder(tuple("updateAccessGroup.groupId", "must not be blank"));

		// Assert
		verifyNoInteractions(accessGroupServiceMock);
	}

	@Test
	void deleteAccessGroupWithInvalidNamespace() {

		// Act
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
			.containsExactlyInAnyOrder(tuple("deleteAccessGroup.namespace", "can only contain A-Z, a-z, 0-9, - and _"));

		// Assert
		verifyNoInteractions(accessGroupServiceMock);
	}

	@Test
	void deleteAccessGroupWithInvalidMunicipalityId() {

		// Act
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
			.containsExactlyInAnyOrder(tuple("deleteAccessGroup.municipalityId", "not a valid municipality ID"));

		// Assert
		verifyNoInteractions(accessGroupServiceMock);
	}

	@Test
	void deleteAccessGroupWithBlankGroupId() {

		// Act
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
			.containsExactlyInAnyOrder(tuple("deleteAccessGroup.groupId", "must not be blank"));

		// Assert
		verifyNoInteractions(accessGroupServiceMock);
	}

}

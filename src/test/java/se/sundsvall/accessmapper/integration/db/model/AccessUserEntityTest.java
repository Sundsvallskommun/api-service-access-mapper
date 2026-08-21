package se.sundsvall.accessmapper.integration.db.model;

import java.util.List;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;

class AccessUserEntityTest {

	@Test
	void testBean() {
		MatcherAssert.assertThat(AccessUserEntity.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void builder() {
		// Arrange
		final var id = "id";
		final var municipalityId = "municipalityId";
		final var namespace = "namespace";
		final var userId = "userId";
		final var accessByType = List.of(new AccessTypeEntity());

		// Act
		final var result = AccessUserEntity.create()
			.withId(id)
			.withMunicipalityId(municipalityId)
			.withNamespace(namespace)
			.withUserId(userId)
			.withAccessByType(accessByType);

		// Assert
		assertThat(result).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(result.getId()).isEqualTo(id);
		assertThat(result.getMunicipalityId()).isEqualTo(municipalityId);
		assertThat(result.getNamespace()).isEqualTo(namespace);
		assertThat(result.getUserId()).isEqualTo(userId);
		assertThat(result.getAccessByType()).isEqualTo(accessByType);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(AccessUserEntity.create()).hasAllNullFieldsOrProperties();
		assertThat(new AccessUserEntity()).hasAllNullFieldsOrProperties();
	}
}

package se.sundsvall.accessmapper.integration.db.model;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;

import java.util.List;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

class AccessGroupEntityTest {

	@Test
	void testBean() {
		MatcherAssert.assertThat(AccessGroupEntity.class, allOf(
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
		final var accessByType = List.of(new AccessTypeEntity());

		// Act
		final var result = AccessGroupEntity.create()
			.withId(id)
			.withMunicipalityId(municipalityId)
			.withNamespace(namespace)
			.withAccessByType(accessByType);

		// Assert
		assertThat(result).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(result.getId()).isEqualTo(id);
		assertThat(result.getMunicipalityId()).isEqualTo(municipalityId);
		assertThat(result.getNamespace()).isEqualTo(namespace);
		assertThat(result.getAccessByType()).isEqualTo(accessByType);

	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(AccessGroupEntity.create()).hasAllNullFieldsOrProperties();
		assertThat(new AccessGroupEntity()).hasAllNullFieldsOrProperties();
	}
}

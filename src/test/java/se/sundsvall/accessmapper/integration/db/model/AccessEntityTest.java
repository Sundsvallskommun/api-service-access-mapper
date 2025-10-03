package se.sundsvall.accessmapper.integration.db.model;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

class AccessEntityTest {
	@Test
	void testBean() {
		MatcherAssert.assertThat(AccessEntity.class, allOf(
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
		final var accessLevel = "accessLevel";
		final var pattern = "pattern";

		// Act
		final var result = AccessEntity.create()
			.withId(id)
			.withPattern(pattern)
			.withAccessLevel(accessLevel);

		// Assert
		assertThat(result).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(result.getId()).isEqualTo(id);
		assertThat(result.getAccessLevel()).isEqualTo(accessLevel);
		assertThat(result.getPattern()).isEqualTo(pattern);

	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(AccessEntity.create()).hasAllNullFieldsOrProperties();
		assertThat(new AccessEntity()).hasAllNullFieldsOrProperties();
	}
}

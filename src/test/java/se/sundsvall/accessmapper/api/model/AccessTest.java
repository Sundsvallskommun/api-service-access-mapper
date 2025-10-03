package se.sundsvall.accessmapper.api.model;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

class AccessTest {

	@Test
	void testBean() {
		MatcherAssert.assertThat(Access.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void builder() {
		// Arrange
		final var pattern = "pattern";
		final var accessLevel = AccessLevel.LR;
		// Act
		final var result = Access.create()
			.withAccessLevel(accessLevel)
			.withPattern(pattern);
		// Assert
		assertThat(result).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(result.getAccessLevel()).isEqualTo(accessLevel);
		assertThat(result.getPattern()).isEqualTo(pattern);

	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(Access.create()).hasAllNullFieldsOrProperties();
		assertThat(new Access()).hasAllNullFieldsOrProperties();
	}

}

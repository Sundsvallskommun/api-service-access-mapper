package se.sundsvall.accessmapper.api.model;

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

class AccessGroupTest {

	@Test
	void testBean() {
		MatcherAssert.assertThat(AccessGroup.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void builder() {
		// Arrange
		final var group = "group";
		final var accessByType = List.of(new AccessType());

		// Act
		final var result = AccessGroup.create()
			.withGroup(group)
			.withAccessByType(accessByType);

		// Assert
		assertThat(result).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(result.getGroup()).isEqualTo(group);
		assertThat(result.getAccessByType()).isEqualTo(accessByType);

	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(AccessGroup.create()).hasAllNullFieldsOrProperties();
		assertThat(new AccessGroup()).hasAllNullFieldsOrProperties();
	}

}

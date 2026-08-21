package se.sundsvall.accessmapper.api.model;

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

class AccessUserTest {

	@Test
	void testBean() {
		MatcherAssert.assertThat(AccessUser.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilderMethods() {
		// Arrange
		final var id = "id";
		final var userId = "userId";
		final var accessByType = List.of(new AccessType());

		// Act
		final var result = AccessUser.create()
			.withId(id)
			.withUserId(userId)
			.withAccessByType(accessByType);

		// Assert
		assertThat(result).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(result.getId()).isEqualTo(id);
		assertThat(result.getUserId()).isEqualTo(userId);
		assertThat(result.getAccessByType()).isEqualTo(accessByType);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(AccessUser.create()).hasAllNullFieldsOrProperties();
		assertThat(new AccessUser()).hasAllNullFieldsOrProperties();
	}
}

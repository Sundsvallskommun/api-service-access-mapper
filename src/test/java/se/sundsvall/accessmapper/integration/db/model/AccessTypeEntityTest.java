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

class AccessTypeEntityTest {

	@Test
	void testBean() {
		MatcherAssert.assertThat(AccessTypeEntity.class, allOf(
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
		final var type = "type";
		final var access = List.of(new AccessEntity());

		// Act
		final var result = AccessTypeEntity.create()
			.withId(id)
			.withType(type)
			.withAccess(access);

		// Assert
		assertThat(result).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(result.getId()).isEqualTo(id);
		assertThat(result.getType()).isEqualTo(type);
		assertThat(result.getAccess()).isEqualTo(access);

	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(AccessTypeEntity.create()).hasAllNullFieldsOrProperties();
		assertThat(new AccessTypeEntity()).hasAllNullFieldsOrProperties();
	}

}

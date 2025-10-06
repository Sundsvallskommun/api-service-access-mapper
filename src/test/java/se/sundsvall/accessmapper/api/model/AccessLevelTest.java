package se.sundsvall.accessmapper.api.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class AccessLevelTest {

	@Test
	void testEnumValues() {
		assertThat(AccessLevel.values()).hasSize(3);
	}

	@Test
	void testEnumRW() {
		assertThat(AccessLevel.valueOf("RW")).isEqualTo(AccessLevel.RW);
		assertThat(AccessLevel.RW.name()).isEqualTo("RW");
	}

	@Test
	void testEnumR() {
		assertThat(AccessLevel.valueOf("R")).isEqualTo(AccessLevel.R);
		assertThat(AccessLevel.R.name()).isEqualTo("R");
	}

	@Test
	void testEnumLR() {
		assertThat(AccessLevel.valueOf("LR")).isEqualTo(AccessLevel.LR);
		assertThat(AccessLevel.LR.name()).isEqualTo("LR");
	}

	@Test
	void testValueOfWithInvalidValue() {
		assertThatThrownBy(() -> AccessLevel.valueOf("INVALID"))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void testValueOfWithNull() {
		assertThatThrownBy(() -> AccessLevel.valueOf(null))
			.isInstanceOf(NullPointerException.class);
	}

}

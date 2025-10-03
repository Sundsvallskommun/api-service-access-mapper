package se.sundsvall.accessmapper.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

@Schema(description = "Access model")
public class Access {
	@Schema(description = "Access pattern", example = "FA/**")
	private String pattern;
	@Schema(description = "Access level", example = "LR")
	private AccessLevel accessLevel;

	public static Access create() {
		return new Access();
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(final String pattern) {
		this.pattern = pattern;
	}

	public Access withPattern(final String pattern) {
		this.pattern = pattern;
		return this;
	}

	public AccessLevel getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(final AccessLevel accessLevel) {
		this.accessLevel = accessLevel;
	}

	public Access withAccessLevel(final AccessLevel accessLevel) {
		this.accessLevel = accessLevel;
		return this;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		final Access access1 = (Access) o;
		return Objects.equals(pattern, access1.pattern) && accessLevel == access1.accessLevel;
	}

	@Override
	public int hashCode() {
		return Objects.hash(pattern, accessLevel);
	}

	@Override
	public String toString() {
		return "Access{" +
			"pattern='" + pattern + '\'' +
			", accessLevel=" + accessLevel +
			'}';
	}
}

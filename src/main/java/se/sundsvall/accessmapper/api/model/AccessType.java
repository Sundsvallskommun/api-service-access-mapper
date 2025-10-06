package se.sundsvall.accessmapper.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Objects;

@Schema(description = "Access type model")
public class AccessType {
	@Schema(description = "Access type", example = "label")
	private String type;
	@Schema(description = "Access by type")
	private List<Access> access;

	public static AccessType create() {
		return new AccessType();
	}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public AccessType withType(final String type) {
		this.type = type;
		return this;
	}

	public List<Access> getAccess() {
		return access;
	}

	public void setAccess(final List<Access> access) {
		this.access = access;
	}

	public AccessType withAccess(final List<Access> access) {
		this.access = access;
		return this;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		final AccessType that = (AccessType) o;
		return Objects.equals(type, that.type) && Objects.equals(access, that.access);
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, access);
	}

	@Override
	public String toString() {
		return "AccessType{" +
			"type='" + type + '\'' +
			", access=" + access +
			'}';
	}
}

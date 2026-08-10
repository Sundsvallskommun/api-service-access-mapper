package se.sundsvall.accessmapper.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Objects;

@Schema(description = "Access user model")
public class AccessUser {

	@Schema(description = "Access user ID", example = "81471222-5798-11e9-ae24-57fa13b361e1", accessMode = Schema.AccessMode.READ_ONLY)
	private String id;

	@Schema(description = "User identifier", example = "joe01doe")
	private String userId;

	@Schema(description = "Access by type")
	private List<AccessType> accessByType;

	public static AccessUser create() {
		return new AccessUser();
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public AccessUser withId(final String id) {
		this.id = id;
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(final String userId) {
		this.userId = userId;
	}

	public AccessUser withUserId(final String userId) {
		this.userId = userId;
		return this;
	}

	public List<AccessType> getAccessByType() {
		return accessByType;
	}

	public void setAccessByType(final List<AccessType> accessByType) {
		this.accessByType = accessByType;
	}

	public AccessUser withAccessByType(final List<AccessType> accessByType) {
		this.accessByType = accessByType;
		return this;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		final AccessUser that = (AccessUser) o;
		return Objects.equals(id, that.id) && Objects.equals(userId, that.userId) && Objects.equals(accessByType, that.accessByType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, userId, accessByType);
	}

	@Override
	public String toString() {
		return "AccessUser{" +
			"id='" + id + '\'' +
			", userId='" + userId + '\'' +
			", accessByType=" + accessByType +
			'}';
	}
}

package se.sundsvall.accessmapper.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Objects;

@Schema(description = "Access group model")
public class AccessGroup {
	@Schema(description = "Access group id", examples = "550e8400-e29b-41d4-a716-446655440000", accessMode = Schema.AccessMode.READ_ONLY)
	private String id;
	@Schema(description = "Access group", examples = "G1")
	private String groupId;
	@Schema(description = "Access by type")
	private List<AccessType> accessByType;

	public static AccessGroup create() {
		return new AccessGroup();
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public AccessGroup withId(final String id) {
		this.id = id;
		return this;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(final String groupId) {
		this.groupId = groupId;
	}

	public AccessGroup withGroupId(final String groupId) {
		this.groupId = groupId;
		return this;
	}

	public List<AccessType> getAccessByType() {
		return accessByType;
	}

	public void setAccessByType(final List<AccessType> accessByType) {
		this.accessByType = accessByType;
	}

	public AccessGroup withAccessByType(final List<AccessType> accessByType) {
		this.accessByType = accessByType;
		return this;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		final AccessGroup that = (AccessGroup) o;
		return Objects.equals(id, that.id) && Objects.equals(groupId, that.groupId) && Objects.equals(accessByType, that.accessByType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, groupId, accessByType);
	}

	@Override
	public String toString() {
		return "AccessGroup{" +
			"id='" + id + '\'' +
			", groupId='" + groupId + '\'' +
			", accessByType=" + accessByType +
			'}';
	}
}

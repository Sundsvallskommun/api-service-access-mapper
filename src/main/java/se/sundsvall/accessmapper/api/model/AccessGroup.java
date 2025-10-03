package se.sundsvall.accessmapper.api.model;

import java.util.List;
import java.util.Objects;

public class AccessGroup {
	private String group;
	private List<AccessType> accessByType;

	public static AccessGroup create() {
		return new AccessGroup();
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(final String group) {
		this.group = group;
	}

	public AccessGroup withGroup(final String group) {
		this.group = group;
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
		return Objects.equals(group, that.group) && Objects.equals(accessByType, that.accessByType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(group, accessByType);
	}

	@Override
	public String toString() {
		return "AccessGroup{" +
			"group='" + group + '\'' +
			", accessByType=" + accessByType +
			'}';
	}
}

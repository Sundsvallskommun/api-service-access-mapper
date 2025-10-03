package se.sundsvall.accessmapper.integration.db.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "access")
public class AccessEntity {

	@Id
	@UuidGenerator
	@Column(name = "id")
	private String id;

	@Column(name = "pattern")
	private String pattern;

	@Column(name = "access_level")
	private String accessLevel;

	public static AccessEntity create() {
		return new AccessEntity();
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public AccessEntity withId(final String id) {
		this.id = id;
		return this;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(final String pattern) {
		this.pattern = pattern;
	}

	public AccessEntity withPattern(final String pattern) {
		this.pattern = pattern;
		return this;
	}

	public String getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(final String accessLevel) {
		this.accessLevel = accessLevel;
	}

	public AccessEntity withAccessLevel(final String accessLevel) {
		this.accessLevel = accessLevel;
		return this;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		final AccessEntity that = (AccessEntity) o;
		return Objects.equals(id, that.id) && Objects.equals(pattern, that.pattern) && Objects.equals(accessLevel, that.accessLevel);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, pattern, accessLevel);
	}

	@Override
	public String toString() {
		return "AccessEntity{" +
			"id='" + id + '\'' +
			", pattern='" + pattern + '\'' +
			", accessLevel='" + accessLevel + '\'' +
			'}';
	}
}

package se.sundsvall.accessmapper.integration.db.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Objects;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "access_type")
public class AccessTypeEntity {

	@Id
	@UuidGenerator
	@Column(name = "id")
	private String id;

	@Column(name = "type")
	private String type;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "access_type_id", foreignKey = @ForeignKey(name = "fk_access_type_id"))
	private List<AccessEntity> access;

	public static AccessTypeEntity create() {
		return new AccessTypeEntity();
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public AccessTypeEntity withId(final String id) {
		this.id = id;
		return this;
	}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public AccessTypeEntity withType(final String type) {
		this.type = type;
		return this;
	}

	public List<AccessEntity> getAccess() {
		return access;
	}

	public void setAccess(final List<AccessEntity> access) {
		this.access = access;
	}

	public AccessTypeEntity withAccess(final List<AccessEntity> access) {
		this.access = access;
		return this;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		final AccessTypeEntity that = (AccessTypeEntity) o;
		return Objects.equals(id, that.id) && Objects.equals(type, that.type) && Objects.equals(access, that.access);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, type, access);
	}

	@Override
	public String toString() {
		return "AccessTypeEntity{" +
			"id='" + id + '\'' +
			", type='" + type + '\'' +
			", access=" + access +
			'}';
	}
}

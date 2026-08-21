package se.sundsvall.accessmapper.integration.db.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.List;
import java.util.Objects;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "access_group", uniqueConstraints = @UniqueConstraint(name = "uq_municipality_id_namespace_group_id", columnNames = {
	"municipality_id", "namespace", "group_id"
}), indexes = {
	@Index(name = "idx_id_group_id", columnList = "id, group_id"),
	@Index(name = "idx_municipality_id_namespace_group_id", columnList = "municipality_id, namespace, group_id")
})
public class AccessGroupEntity {

	@Id
	@UuidGenerator
	@Column(name = "id")
	private String id;

	@Column(name = "municipality_id")
	private String municipalityId;

	@Column(name = "namespace")
	private String namespace;

	@Column(name = "group_id", length = 36)
	private String groupId;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "access_group_id", foreignKey = @ForeignKey(name = "fk_access_group_id"))
	private List<AccessTypeEntity> accessByType;

	public static AccessGroupEntity create() {
		return new AccessGroupEntity();
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public AccessGroupEntity withId(final String id) {
		this.id = id;
		return this;
	}

	public String getMunicipalityId() {
		return municipalityId;
	}

	public void setMunicipalityId(final String municipalityId) {
		this.municipalityId = municipalityId;
	}

	public AccessGroupEntity withMunicipalityId(final String municipalityId) {
		this.municipalityId = municipalityId;
		return this;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(final String namespace) {
		this.namespace = namespace;
	}

	public AccessGroupEntity withNamespace(final String namespace) {
		this.namespace = namespace;
		return this;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(final String groupId) {
		this.groupId = groupId;
	}

	public AccessGroupEntity withGroupId(final String groupId) {
		this.groupId = groupId;
		return this;
	}

	public List<AccessTypeEntity> getAccessByType() {
		return accessByType;
	}

	public void setAccessByType(final List<AccessTypeEntity> accessByType) {
		this.accessByType = accessByType;
	}

	public AccessGroupEntity withAccessByType(final List<AccessTypeEntity> accessByType) {
		this.accessByType = accessByType;
		return this;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		final AccessGroupEntity that = (AccessGroupEntity) o;
		return Objects.equals(id, that.id) && Objects.equals(municipalityId, that.municipalityId) && Objects.equals(namespace, that.namespace) && Objects.equals(groupId, that.groupId) && Objects.equals(accessByType, that.accessByType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, municipalityId, namespace, groupId, accessByType);
	}

	@Override
	public String toString() {
		return "AccessGroupEntity{" +
			"id='" + id + '\'' +
			", municipalityId='" + municipalityId + '\'' +
			", namespace='" + namespace + '\'' +
			", groupId='" + groupId + '\'' +
			", accessByType=" + accessByType +
			'}';
	}
}

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

@Entity
@Table(name = "access_group")
public class AccessGroupEntity {

	@Id
	private String groupId;

	@Column(name = "municipality_id")
	private String municipalityId;

	@Column(name = "namespace")
	private String namespace;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "access_group_id", foreignKey = @ForeignKey(name = "fk_access_group_id"))
	private List<AccessTypeEntity> accessByType;

	public static AccessGroupEntity create() {
		return new AccessGroupEntity();
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
		return Objects.equals(groupId, that.groupId) && Objects.equals(municipalityId, that.municipalityId) && Objects.equals(namespace, that.namespace) && Objects.equals(accessByType, that.accessByType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(groupId, municipalityId, namespace, accessByType);
	}

	@Override
	public String toString() {
		return "AccessGroupEntity{" +
			"groupId='" + groupId + '\'' +
			", municipalityId='" + municipalityId + '\'' +
			", namespace='" + namespace + '\'' +
			", accessByType=" + accessByType +
			'}';
	}
}

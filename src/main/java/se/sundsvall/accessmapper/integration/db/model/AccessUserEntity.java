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
@Table(name = "access_user")
public class AccessUserEntity {

	@Id
	@UuidGenerator
	@Column(name = "id")
	private String id;

	@Column(name = "municipality_id")
	private String municipalityId;

	@Column(name = "namespace")
	private String namespace;

	@Column(name = "user_id")
	private String userId;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "access_user_id", foreignKey = @ForeignKey(name = "fk_access_user_id"))
	private List<AccessTypeEntity> accessByType;

	public static AccessUserEntity create() {
		return new AccessUserEntity();
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public AccessUserEntity withId(final String id) {
		this.id = id;
		return this;
	}

	public String getMunicipalityId() {
		return municipalityId;
	}

	public void setMunicipalityId(final String municipalityId) {
		this.municipalityId = municipalityId;
	}

	public AccessUserEntity withMunicipalityId(final String municipalityId) {
		this.municipalityId = municipalityId;
		return this;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(final String namespace) {
		this.namespace = namespace;
	}

	public AccessUserEntity withNamespace(final String namespace) {
		this.namespace = namespace;
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(final String userId) {
		this.userId = userId;
	}

	public AccessUserEntity withUserId(final String userId) {
		this.userId = userId;
		return this;
	}

	public List<AccessTypeEntity> getAccessByType() {
		return accessByType;
	}

	public void setAccessByType(final List<AccessTypeEntity> accessByType) {
		this.accessByType = accessByType;
	}

	public AccessUserEntity withAccessByType(final List<AccessTypeEntity> accessByType) {
		this.accessByType = accessByType;
		return this;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		final AccessUserEntity that = (AccessUserEntity) o;
		return Objects.equals(id, that.id) && Objects.equals(municipalityId, that.municipalityId) && Objects.equals(namespace, that.namespace) && Objects.equals(userId, that.userId) && Objects.equals(accessByType, that.accessByType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, municipalityId, namespace, userId, accessByType);
	}

	@Override
	public String toString() {
		return "AccessUserEntity{" +
			"id='" + id + '\'' +
			", municipalityId='" + municipalityId + '\'' +
			", namespace='" + namespace + '\'' +
			", userId='" + userId + '\'' +
			", accessByType=" + accessByType +
			'}';
	}
}

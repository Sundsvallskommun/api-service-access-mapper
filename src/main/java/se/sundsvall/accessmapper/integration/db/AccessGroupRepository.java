package se.sundsvall.accessmapper.integration.db;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import se.sundsvall.accessmapper.integration.db.model.AccessGroupEntity;

@CircuitBreaker(name = "accessGroupRepository")
public interface AccessGroupRepository extends JpaRepository<AccessGroupEntity, Long>, JpaSpecificationExecutor<AccessGroupEntity> {

	AccessGroupEntity findByMunicipalityIdAndNamespaceAndId(String municipalityId, String namespace, String id);

	boolean existsByMunicipalityIdAndNamespaceAndId(String municipalityId, String namespace, String id);
}

package se.sundsvall.accessmapper.integration.db;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import se.sundsvall.accessmapper.integration.db.model.AccessUserEntity;

@CircuitBreaker(name = "accessUserRepository")
public interface AccessUserRepository extends JpaRepository<AccessUserEntity, String>, JpaSpecificationExecutor<AccessUserEntity> {

	Optional<AccessUserEntity> findByMunicipalityIdAndNamespaceAndId(String municipalityId, String namespace, String id);

	List<AccessUserEntity> findAllByMunicipalityIdAndNamespaceAndUserId(String municipalityId, String namespace, String userId);
}

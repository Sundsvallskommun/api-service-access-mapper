package se.sundsvall.accessmapper.integration.db;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import se.sundsvall.accessmapper.integration.db.model.AccessUserEntity;

@CircuitBreaker(name = "accessUserRepository")
public interface AccessUserRepository extends JpaRepository<AccessUserEntity, String> {

	List<AccessUserEntity> findAllByMunicipalityIdAndNamespace(String municipalityId, String namespace);

	Optional<AccessUserEntity> findByMunicipalityIdAndNamespaceAndId(String municipalityId, String namespace, String id);

	List<AccessUserEntity> findAllByMunicipalityIdAndNamespaceAndUserId(String municipalityId, String namespace, String userId);
}

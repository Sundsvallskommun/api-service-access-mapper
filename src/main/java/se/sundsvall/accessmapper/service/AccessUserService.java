package se.sundsvall.accessmapper.service;

import java.util.List;
import org.springframework.stereotype.Service;
import se.sundsvall.accessmapper.api.model.AccessUser;
import se.sundsvall.accessmapper.integration.db.AccessUserRepository;
import se.sundsvall.accessmapper.integration.db.model.AccessUserEntity;
import se.sundsvall.dept44.problem.Problem;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static se.sundsvall.accessmapper.service.mapper.Mapper.toAccessUser;
import static se.sundsvall.accessmapper.service.mapper.Mapper.toAccessUserEntity;
import static se.sundsvall.accessmapper.service.mapper.Mapper.toAccessUsers;
import static se.sundsvall.accessmapper.service.mapper.Mapper.updateAccessUserEntity;
import static se.sundsvall.dept44.util.LogUtils.sanitizeForLogging;

@Service
public class AccessUserService {

	private final AccessUserRepository accessUserRepository;

	public AccessUserService(final AccessUserRepository accessUserRepository) {
		this.accessUserRepository = accessUserRepository;
	}

	public List<AccessUser> getAccessUsers(final String municipalityId, final String namespace) {
		return toAccessUsers(accessUserRepository.findAllByMunicipalityIdAndNamespace(municipalityId, namespace));
	}

	public AccessUser getAccessUser(final String municipalityId, final String namespace, final String id) {
		return toAccessUser(getAccessUserEntity(municipalityId, namespace, id));
	}

	public AccessUser createAccessUser(final String municipalityId, final String namespace, final AccessUser accessUser) {
		final var entity = toAccessUserEntity(municipalityId, namespace, accessUser);
		final var savedEntity = accessUserRepository.save(entity);
		return toAccessUser(savedEntity);
	}

	public void updateAccessUser(final String municipalityId, final String namespace, final String id, final AccessUser accessUser) {
		final var existingEntity = getAccessUserEntity(municipalityId, namespace, id);
		updateAccessUserEntity(existingEntity, accessUser);
		accessUserRepository.save(existingEntity);
	}

	public void deleteAccessUser(final String municipalityId, final String namespace, final String id) {
		final var entity = getAccessUserEntity(municipalityId, namespace, id);
		accessUserRepository.delete(entity);
	}

	private AccessUserEntity getAccessUserEntity(final String municipalityId, final String namespace, final String id) {
		return accessUserRepository.findByMunicipalityIdAndNamespaceAndId(municipalityId, namespace, id)
			.orElseThrow(() -> Problem.valueOf(NOT_FOUND,
				"Access user not found for municipalityId: %s, namespace: %s, id: %s.".formatted(
					sanitizeForLogging(municipalityId),
					sanitizeForLogging(namespace),
					sanitizeForLogging(id))));
	}
}

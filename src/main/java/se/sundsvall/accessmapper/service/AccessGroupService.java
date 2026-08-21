package se.sundsvall.accessmapper.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import se.sundsvall.accessmapper.api.model.AccessGroup;
import se.sundsvall.accessmapper.integration.db.AccessGroupRepository;
import se.sundsvall.accessmapper.integration.db.model.AccessGroupEntity;
import se.sundsvall.dept44.problem.Problem;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static se.sundsvall.accessmapper.service.mapper.Mapper.toAccessGroup;
import static se.sundsvall.accessmapper.service.mapper.Mapper.toAccessGroupEntity;
import static se.sundsvall.accessmapper.service.mapper.Mapper.toAccessGroups;
import static se.sundsvall.accessmapper.service.util.SpecificationBuilder.withAccessType;
import static se.sundsvall.accessmapper.service.util.SpecificationBuilder.withMunicipalityId;
import static se.sundsvall.accessmapper.service.util.SpecificationBuilder.withNamespace;
import static se.sundsvall.dept44.util.LogUtils.sanitizeForLogging;

@Service
public class AccessGroupService {

	private final AccessGroupRepository accessGroupRepository;

	public AccessGroupService(final AccessGroupRepository accessGroupRepository) {
		this.accessGroupRepository = accessGroupRepository;
	}

	public AccessGroup getAccessGroup(final String municipalityId, final String namespace, final String id) {

		return toAccessGroup(getAccessGroupEntity(municipalityId, namespace, id));
	}

	public List<AccessGroup> getAccessGroups(final String municipalityId, final String namespace, final String type) {
		final var specification = withMunicipalityId(municipalityId)
			.and(withNamespace(namespace))
			.and(withAccessType(type));

		return toAccessGroups(accessGroupRepository.findAll(specification));
	}

	public void createAccessGroup(final String municipalityId, final String namespace, final AccessGroup accessGroup) {

		if (accessGroupRepository.existsByMunicipalityIdAndNamespaceAndGroupId(municipalityId, namespace, accessGroup.getGroupId())) {
			throw Problem.valueOf(CONFLICT,
				"Access group already exists for municipalityId: %s, namespace: %s, groupId: %s.".formatted(
					sanitizeForLogging(municipalityId),
					sanitizeForLogging(namespace),
					sanitizeForLogging(accessGroup.getGroupId())));
		}
		final var entity = toAccessGroupEntity(municipalityId, namespace, accessGroup);
		accessGroupRepository.save(entity);
	}

	public void updateAccessGroup(final String municipalityId, final String namespace, final String id, final AccessGroup accessGroup) {
		final var existingEntity = getAccessGroupEntity(municipalityId, namespace, id);
		final var newEntity = toAccessGroupEntity(municipalityId, namespace, accessGroup);
		newEntity.setId(existingEntity.getId());

		accessGroupRepository.save(newEntity);
	}

	public void deleteAccessGroup(final String municipalityId, final String namespace, final String id) {

		final var entity = getAccessGroupEntity(municipalityId, namespace, id);
		accessGroupRepository.delete(entity);
	}

	public AccessGroupEntity getAccessGroupEntity(final String municipalityId, final String namespace, final String id) {
		return Optional.ofNullable(accessGroupRepository.findByMunicipalityIdAndNamespaceAndId(municipalityId, namespace, id))
			.orElseThrow(() -> Problem.valueOf(NOT_FOUND,
				"Access group not found for municipalityId: %s, namespace: %s, id: %s.".formatted(
					sanitizeForLogging(municipalityId),
					sanitizeForLogging(namespace),
					sanitizeForLogging(id))));
	}

}

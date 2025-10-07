package se.sundsvall.accessmapper.service;

import static org.zalando.problem.Status.NOT_FOUND;
import static se.sundsvall.accessmapper.service.mapper.Mapper.toAccessGroup;
import static se.sundsvall.accessmapper.service.mapper.Mapper.toAccessGroupEntity;
import static se.sundsvall.accessmapper.service.mapper.Mapper.toAccessGroups;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import se.sundsvall.accessmapper.api.model.AccessGroup;
import se.sundsvall.accessmapper.integration.db.AccessGroupRepository;
import se.sundsvall.accessmapper.integration.db.model.AccessGroupEntity;

@Service
public class AccessGroupService {

	private final AccessGroupRepository accessGroupRepository;

	public AccessGroupService(final AccessGroupRepository accessGroupRepository) {
		this.accessGroupRepository = accessGroupRepository;
	}

	public AccessGroup getAccessGroup(final String municipalityId, final String namespace, final String groupId) {

		return toAccessGroup(getAccessGroupEntity(municipalityId, namespace, groupId));
	}

	public List<AccessGroup> getAccessGroups(final String municipalityId, final String namespace, final String type) {
		return toAccessGroups(accessGroupRepository.findByMunicipalityIdAndNamespaceAndAccessByType_Type(municipalityId, namespace, type));
	}

	public void createAccessGroup(final String municipalityId, final String namespace, final String groupId, final AccessGroup accessGroup) {
		final var entity = toAccessGroupEntity(municipalityId, namespace, groupId, accessGroup);
		accessGroupRepository.save(entity);
	}

	public void updateAccessGroup(final String municipalityId, final String namespace, final String groupId, final AccessGroup accessGroup) {
		getAccessGroupEntity(municipalityId, namespace, groupId);
		final var newEntity = toAccessGroupEntity(municipalityId, namespace, groupId, accessGroup);

		accessGroupRepository.save(newEntity);
	}

	public void deleteAccessGroup(final String municipalityId, final String namespace, final String groupId) {

		final var entity = getAccessGroupEntity(municipalityId, namespace, groupId);
		accessGroupRepository.delete(entity);
	}

	public AccessGroupEntity getAccessGroupEntity(final String municipalityId, final String namespace, final String groupId) {
		return Optional.ofNullable(accessGroupRepository.findByMunicipalityIdAndNamespaceAndId(municipalityId, namespace, groupId))
			.orElseThrow(() -> Problem.valueOf(NOT_FOUND, "Access group not found."));
	}

}

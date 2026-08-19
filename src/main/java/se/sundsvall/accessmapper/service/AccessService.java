package se.sundsvall.accessmapper.service;

import generated.se.sundsvall.activedirectory.OUChildren;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import se.sundsvall.accessmapper.api.model.AccessGroup;
import se.sundsvall.accessmapper.integration.activedirectory.ActiveDirectoryClient;
import se.sundsvall.accessmapper.integration.activedirectory.configuration.ActiveDirectoryProperties;
import se.sundsvall.accessmapper.integration.db.AccessGroupRepository;
import se.sundsvall.accessmapper.integration.db.AccessUserRepository;
import se.sundsvall.accessmapper.service.mapper.Mapper;
import se.sundsvall.dept44.problem.ThrowableProblem;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static se.sundsvall.accessmapper.service.mapper.Mapper.toAccessGroups;

@Service
public class AccessService {

	private final ActiveDirectoryClient activeDirectoryClient;
	private final ActiveDirectoryProperties activeDirectoryProperties;
	private final AccessGroupRepository accessGroupRepository;
	private final AccessUserRepository accessUserRepository;

	public AccessService(final ActiveDirectoryClient activeDirectoryClient, final ActiveDirectoryProperties activeDirectoryProperties, final AccessGroupRepository accessGroupRepository, final AccessUserRepository accessUserRepository) {
		this.activeDirectoryClient = activeDirectoryClient;
		this.activeDirectoryProperties = activeDirectoryProperties;
		this.accessGroupRepository = accessGroupRepository;
		this.accessUserRepository = accessUserRepository;
	}

	public List<AccessGroup> getAccessDetails(final String municipalityId, final String namespace, final String adId, final String type) {

		List<OUChildren> adGroups;
		try {
			adGroups = activeDirectoryClient.getGroupsForUser(municipalityId, activeDirectoryProperties.domain(), adId);
		} catch (final ThrowableProblem e) {
			if (NOT_FOUND == e.getStatus()) {
				adGroups = Collections.emptyList();
			} else {
				throw e;
			}
		}

		final var accessGroups = adGroups.stream()
			.map(OUChildren::getGuid)
			.filter(Objects::nonNull)
			.map(guid -> accessGroupRepository.findByMunicipalityIdAndNamespaceAndGroupId(municipalityId, namespace, guid.toString()))
			.filter(Objects::nonNull)
			.filter(accessGroup -> type == null || accessGroup.getAccessByType().stream()
				.anyMatch(accessType -> type.equals(accessType.getType())))
			.toList();

		final var result = new ArrayList<>(toAccessGroups(accessGroups));

		accessUserRepository.findAllByMunicipalityIdAndNamespaceAndUserId(municipalityId, namespace, adId)
			.stream()
			.filter(user -> type == null || user.getAccessByType().stream()
				.anyMatch(accessType -> type.equals(accessType.getType())))
			.map(Mapper::toAccessGroupFromUser)
			.forEach(result::add);

		return result;
	}
}

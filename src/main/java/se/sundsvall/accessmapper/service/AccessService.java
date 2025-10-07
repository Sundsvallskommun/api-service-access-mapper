package se.sundsvall.accessmapper.service;

import static se.sundsvall.accessmapper.service.mapper.Mapper.toAccessGroups;

import generated.se.sundsvall.activedirectory.OUChildren;
import java.util.List;
import org.springframework.stereotype.Service;
import se.sundsvall.accessmapper.api.model.AccessGroup;
import se.sundsvall.accessmapper.integration.activedirectory.ActiveDirectoryClient;
import se.sundsvall.accessmapper.integration.db.AccessGroupRepository;

@Service
public class AccessService {

	private static final String DOMAIN = "personal";

	private final ActiveDirectoryClient activeDirectoryClient;
	private final AccessGroupRepository accessGroupRepository;

	public AccessService(final ActiveDirectoryClient activeDirectoryClient, final AccessGroupRepository accessGroupRepository) {
		this.activeDirectoryClient = activeDirectoryClient;
		this.accessGroupRepository = accessGroupRepository;
	}

	public List<AccessGroup> getAccessDetails(final String municipalityId, final String namespace, final String adId, final String type) {

		final var accessGroups = activeDirectoryClient.getGroupsForUser(municipalityId, DOMAIN, adId)
			.stream()
			.map(OUChildren::getGuid)
			.toList()
			.stream()
			.map(guid -> accessGroupRepository.findByMunicipalityIdAndNamespaceAndId(municipalityId, namespace, guid.toString()))
			.filter(accessGroup -> type == null || accessGroup.getAccessByType().stream()
				.anyMatch(accessType -> type.equals(accessType.getType())))
			.toList();

		return toAccessGroups(accessGroups);
	}
}

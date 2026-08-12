package se.sundsvall.accessmapper.service;

import generated.se.sundsvall.activedirectory.OUChildren;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import se.sundsvall.accessmapper.api.model.AccessGroup;
import se.sundsvall.accessmapper.integration.activedirectory.ActiveDirectoryClient;
import se.sundsvall.accessmapper.integration.db.AccessGroupRepository;
import se.sundsvall.accessmapper.integration.db.AccessUserRepository;
import se.sundsvall.accessmapper.service.mapper.Mapper;

import static se.sundsvall.accessmapper.service.mapper.Mapper.toAccessGroups;

@Service
public class AccessService {

	private static final String DOMAIN = "personal";

	private final ActiveDirectoryClient activeDirectoryClient;
	private final AccessGroupRepository accessGroupRepository;
	private final AccessUserRepository accessUserRepository;

	public AccessService(final ActiveDirectoryClient activeDirectoryClient, final AccessGroupRepository accessGroupRepository, final AccessUserRepository accessUserRepository) {
		this.activeDirectoryClient = activeDirectoryClient;
		this.accessGroupRepository = accessGroupRepository;
		this.accessUserRepository = accessUserRepository;
	}

	public List<AccessGroup> getAccessDetails(final String municipalityId, final String namespace, final String adId, final String type) {

		final var accessGroups = activeDirectoryClient.getGroupsForUser(municipalityId, DOMAIN, adId)
			.stream()
			.map(OUChildren::getGuid)
			.filter(Objects::nonNull)
			.map(guid -> accessGroupRepository.findByMunicipalityIdAndNamespaceAndId(municipalityId, namespace, guid.toString()))
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

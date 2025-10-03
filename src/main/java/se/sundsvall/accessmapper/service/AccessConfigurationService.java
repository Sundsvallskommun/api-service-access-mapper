package se.sundsvall.accessmapper.service;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;
import se.sundsvall.accessmapper.api.model.AccessGroup;
import se.sundsvall.dept44.util.jacoco.ExcludeFromJacocoGeneratedCoverageReport;

@Service
@ExcludeFromJacocoGeneratedCoverageReport // TODO: remove when implementation is done
public class AccessConfigurationService {

	public AccessGroup getAccessConfiguration(final String municipalityId, final String namespace, final String groupId) {
		// Implementation comes later
		return null;
	}

	public List<AccessGroup> getAccessConfigurations(final String municipalityId, final String namespace, final String type) {
		// Implementation comes later
		return Collections.emptyList();
	}

	public void createAccessConfiguration(final String municipalityId, final String namespace, final String groupId, final AccessGroup accessGroup) {
		// Implementation comes later
	}

	public void updateAccessConfiguration(final String municipalityId, final String namespace, final String groupId, final AccessGroup accessGroup) {
		// Implementation comes later
	}

	public void deleteAccessConfiguration(final String municipalityId, final String namespace, final String groupId) {
		// Implementation comes later
	}

}

package se.sundsvall.accessmapper.service;

import static java.util.Collections.emptyList;

import java.util.List;
import org.springframework.stereotype.Service;
import se.sundsvall.accessmapper.api.model.AccessGroup;
import se.sundsvall.dept44.util.jacoco.ExcludeFromJacocoGeneratedCoverageReport;

@Service
@ExcludeFromJacocoGeneratedCoverageReport // TODO: remove when implementation is done
public class AccessService {

	public List<AccessGroup> getAccessDetails(final String municipalityId, final String namespace, final String adId, final String type) {
		return emptyList();
	}
}

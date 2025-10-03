package se.sundsvall.accessmapper.integration.activedirectory;

import static se.sundsvall.accessmapper.integration.activedirectory.configuration.ActiveDirectoryConfiguration.CLIENT_ID;

import generated.se.sundsvall.activedirectory.OUChildren;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import se.sundsvall.accessmapper.integration.activedirectory.configuration.ActiveDirectoryConfiguration;

@FeignClient(name = CLIENT_ID, url = "${integration.eventlog.url}", configuration = ActiveDirectoryConfiguration.class)
@CircuitBreaker(name = CLIENT_ID)
public interface ActiveDirectoryClient {

	/**
	 * Retrieves the active directory groups for a given user. Returns 404 if the user is not found.
	 *
	 * @param  municipalityId the municipality id to search in
	 * @param  domain         the domain to search in
	 * @param  user           the user to search for
	 * @return                the groups for the user
	 */
	@GetMapping(path = "/{municipalityId}/usergroups/{domain}/{user}")
	List<OUChildren> getGroupsForUser(@PathVariable String municipalityId, @PathVariable String domain, @PathVariable String user);

}

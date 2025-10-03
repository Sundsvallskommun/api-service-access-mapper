package se.sundsvall.accessmapper.api;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;
import static se.sundsvall.accessmapper.Constants.NAMESPACE_REGEXP;
import static se.sundsvall.accessmapper.Constants.NAMESPACE_VALIDATION_MESSAGE;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;
import se.sundsvall.accessmapper.api.model.AccessGroup;
import se.sundsvall.accessmapper.service.AccessConfigurationService;
import se.sundsvall.dept44.common.validators.annotation.ValidMunicipalityId;

@RestController
@Validated
@RequestMapping("/{municipalityId}/{namespace}/access-config/group")
@Tag(name = "Access configuration", description = "Access configuration resources")
@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(oneOf = {
	Problem.class, ConstraintViolationProblem.class
})))
@ApiResponse(responseCode = "500", description = "Internal Server error", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
class AccessConfigurationResource {

	private final AccessConfigurationService accessConfigurationService;

	AccessConfigurationResource(final AccessConfigurationService accessConfigurationService) {
		this.accessConfigurationService = accessConfigurationService;
	}

	@GetMapping(produces = APPLICATION_JSON_VALUE)
	@ApiResponse(responseCode = "200", description = "Successful operation")
	ResponseEntity<List<AccessGroup>> getAccessConfigurations(
		@Parameter(name = "municipalityId", description = "Municipality ID", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "namespace", description = "Namespace", example = "MY_NAMESPACE") @Pattern(regexp = NAMESPACE_REGEXP, message = NAMESPACE_VALIDATION_MESSAGE) @PathVariable final String namespace,
		@Parameter(name = "type", description = "Filter type", example = "label") @RequestParam(required = false) final String type) {
		return ResponseEntity.ok(accessConfigurationService.getAccessConfigurations(municipalityId, namespace, type));
	}

	@GetMapping(path = "/{groupId}", produces = APPLICATION_JSON_VALUE)
	@ApiResponse(responseCode = "200", description = "Successful operation", useReturnTypeSchema = true)
	@ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	ResponseEntity<AccessGroup> getAccessConfiguration(
		@Parameter(name = "municipalityId", description = "Municipality ID", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "namespace", description = "Namespace", example = "MY_NAMESPACE") @Pattern(regexp = NAMESPACE_REGEXP, message = NAMESPACE_VALIDATION_MESSAGE) @PathVariable final String namespace,
		@Parameter(name = "groupId", description = "Group ID", example = "group123") @NotBlank @PathVariable final String groupId) {
		return ResponseEntity.ok(accessConfigurationService.getAccessConfiguration(municipalityId, namespace, groupId));
	}

	@PostMapping(path = "/{groupId}", consumes = APPLICATION_JSON_VALUE, produces = ALL_VALUE)
	@ApiResponse(responseCode = "201", description = "Successfully created", useReturnTypeSchema = true)
	ResponseEntity<AccessGroup> createAccessConfiguration(
		@Parameter(name = "municipalityId", description = "Municipality ID", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "namespace", description = "Namespace", example = "MY_NAMESPACE") @Pattern(regexp = NAMESPACE_REGEXP, message = NAMESPACE_VALIDATION_MESSAGE) @PathVariable final String namespace,
		@Parameter(name = "groupId", description = "Group ID", example = "group123") @NotBlank @PathVariable final String groupId,
		@RequestBody @Valid final AccessGroup accessGroup) {
		accessConfigurationService.createAccessConfiguration(municipalityId, namespace, groupId, accessGroup);
		return created(fromPath("/{municipalityId}/{namespace}/access-config/group/{groupId}")
			.buildAndExpand(municipalityId, namespace, groupId).toUri())
			.header(CONTENT_TYPE, ALL_VALUE)
			.build();
	}

	@PutMapping(path = "/{groupId}", consumes = APPLICATION_JSON_VALUE, produces = ALL_VALUE)
	@ApiResponse(responseCode = "204", description = "No content - Successful operation", useReturnTypeSchema = true)
	@ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	ResponseEntity<Void> updateAccessConfiguration(
		@Parameter(name = "municipalityId", description = "Municipality ID", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "namespace", description = "Namespace", example = "MY_NAMESPACE") @Pattern(regexp = NAMESPACE_REGEXP, message = NAMESPACE_VALIDATION_MESSAGE) @PathVariable final String namespace,
		@Parameter(name = "groupId", description = "Group ID", example = "group123") @NotBlank @PathVariable final String groupId,
		@RequestBody @Valid final AccessGroup accessGroup) {
		accessConfigurationService.updateAccessConfiguration(municipalityId, namespace, groupId, accessGroup);
		return noContent()
			.header(CONTENT_TYPE, ALL_VALUE)
			.build();
	}

	@DeleteMapping(path = "/{groupId}", produces = ALL_VALUE)
	@ApiResponse(responseCode = "204", description = "No content - Successful operation", useReturnTypeSchema = true)
	@ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	ResponseEntity<Void> deleteAccessConfiguration(
		@Parameter(name = "municipalityId", description = "Municipality ID", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "namespace", description = "Namespace", example = "MY_NAMESPACE") @Pattern(regexp = NAMESPACE_REGEXP, message = NAMESPACE_VALIDATION_MESSAGE) @PathVariable final String namespace,
		@Parameter(name = "groupId", description = "Group ID", example = "group123") @NotBlank @PathVariable final String groupId) {

		accessConfigurationService.deleteAccessConfiguration(municipalityId, namespace, groupId);
		return noContent()
			.header(CONTENT_TYPE, ALL_VALUE)
			.build();
	}
}

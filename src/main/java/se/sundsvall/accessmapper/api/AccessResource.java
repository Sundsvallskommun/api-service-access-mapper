package se.sundsvall.accessmapper.api;

import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static se.sundsvall.accessmapper.Constants.NAMESPACE_REGEXP;
import static se.sundsvall.accessmapper.Constants.NAMESPACE_VALIDATION_MESSAGE;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;
import se.sundsvall.accessmapper.api.model.AccessGroup;
import se.sundsvall.accessmapper.service.AccessService;
import se.sundsvall.dept44.common.validators.annotation.ValidMunicipalityId;

@RestController
@Validated
@RequestMapping("/{municipalityId}/{namespace}/access")
@Tag(name = "Access configuration", description = "Access configuration resources")
@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(oneOf = {
	Problem.class, ConstraintViolationProblem.class
})))
@ApiResponse(responseCode = "500", description = "Internal Server error", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
class AccessResource {

	private final AccessService accessService;

	AccessResource(final AccessService accessService) {
		this.accessService = accessService;
	}

	@GetMapping(path = "/ad/{adId}")
	@ApiResponse(responseCode = "200", description = "Successful operation", useReturnTypeSchema = true)
	@ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	ResponseEntity<List<AccessGroup>> getAccessDetails(
		@Parameter(name = "municipalityId", description = "Municipality ID", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "namespace", description = "Namespace", example = "MY_NAMESPACE") @Pattern(regexp = NAMESPACE_REGEXP, message = NAMESPACE_VALIDATION_MESSAGE) @PathVariable final String namespace,
		@Parameter(name = "adId", description = "AdId for the user to get details about", example = "joe01doe") @PathVariable final String adId,
		@Parameter(name = "type", description = "Filter type", example = "label") @RequestParam(required = false) final String type) {
		return ResponseEntity.ok(accessService.getAccessDetails(municipalityId, namespace, adId, type));
	}

}

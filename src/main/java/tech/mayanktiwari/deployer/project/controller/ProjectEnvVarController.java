package tech.mayanktiwari.deployer.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.mayanktiwari.deployer.auth.dto.AuthPrincipal;
import tech.mayanktiwari.deployer.common.openapi.CommonApiResponses;
import tech.mayanktiwari.deployer.project.dto.ProjectEnvVarDTO;
import tech.mayanktiwari.deployer.project.service.ProjectEnvVarService;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/env")
@RequiredArgsConstructor
@Tag(name = "Project Env Vars", description = "Project environment variable management")
public class ProjectEnvVarController {

  private final ProjectEnvVarService projectEnvVarService;

  @PostMapping
  @CommonApiResponses
  @Operation(
      summary = "Batch upsert env vars",
      description = "Creates or updates a list of environment variables for a project.",
      security = @SecurityRequirement(name = "bearer-jwt"))
  @ApiResponse(responseCode = "200", description = "Env vars upserted successfully")
  public ResponseEntity<List<ProjectEnvVarDTO>> upsertEnvVars(
      @PathVariable UUID projectId,
      @RequestBody @Valid List<ProjectEnvVarDTO> dtos,
      @AuthenticationPrincipal AuthPrincipal authPrincipal) {
    return ResponseEntity.ok(
        projectEnvVarService.upsertEnvVars(projectId, authPrincipal.userId(), dtos));
  }

  @GetMapping
  @CommonApiResponses
  @Operation(
      summary = "List env vars",
      description = "Returns all environment variables for a project, decrypted.",
      security = @SecurityRequirement(name = "bearer-jwt"))
  @ApiResponse(responseCode = "200", description = "Env vars returned")
  public ResponseEntity<List<ProjectEnvVarDTO>> getEnvVars(
      @PathVariable UUID projectId, @AuthenticationPrincipal AuthPrincipal authPrincipal) {
    return ResponseEntity.ok(projectEnvVarService.getEnvVars(projectId, authPrincipal.userId()));
  }

  @PutMapping("/{key}")
  @CommonApiResponses
  @Operation(
      summary = "Update an env var",
      description = "Updates the value of a single environment variable.",
      security = @SecurityRequirement(name = "bearer-jwt"))
  @ApiResponse(responseCode = "200", description = "Env var updated")
  public ResponseEntity<ProjectEnvVarDTO> updateEnvVar(
      @PathVariable UUID projectId,
      @PathVariable String key,
      @RequestBody @Valid ProjectEnvVarDTO dto,
      @AuthenticationPrincipal AuthPrincipal authPrincipal) {
    return ResponseEntity.ok(
        projectEnvVarService.updateEnvVar(projectId, key, authPrincipal.userId(), dto));
  }

  @DeleteMapping("/{key}")
  @CommonApiResponses
  @Operation(
      summary = "Delete an env var",
      description = "Deletes a single environment variable by key.",
      security = @SecurityRequirement(name = "bearer-jwt"))
  @ApiResponse(responseCode = "204", description = "Env var deleted")
  public ResponseEntity<Void> deleteEnvVar(
      @PathVariable UUID projectId,
      @PathVariable String key,
      @AuthenticationPrincipal AuthPrincipal authPrincipal) {
    projectEnvVarService.deleteEnvVar(projectId, key, authPrincipal.userId());
    return ResponseEntity.noContent().build();
  }
}

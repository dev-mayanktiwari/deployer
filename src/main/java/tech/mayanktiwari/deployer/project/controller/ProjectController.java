package tech.mayanktiwari.deployer.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.mayanktiwari.deployer.auth.dto.AuthPrincipal;
import tech.mayanktiwari.deployer.common.openapi.CommonApiResponses;
import tech.mayanktiwari.deployer.common.response.ApiError;
import tech.mayanktiwari.deployer.project.dto.ProjectRequestDTO;
import tech.mayanktiwari.deployer.project.dto.ProjectResponseDTO;
import tech.mayanktiwari.deployer.project.service.ProjectService;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Tag(name = "Projects", description = "Project management endpoints")
public class ProjectController {

  private final ProjectService projectService;

  @PostMapping
  @CommonApiResponses
  @Operation(
      summary = "Create a project",
      description = "Creates a new project linked to the authenticated user's GitHub repository.",
      security = @SecurityRequirement(name = "bearer-jwt"))
  @ApiResponse(responseCode = "201", description = "Project created successfully")
  @ApiResponse(
      responseCode = "400",
      description = "Validation failed",
      content = @Content(schema = @Schema(implementation = ApiError.class)))
  @ApiResponse(
      responseCode = "409",
      description = "Project with this name already exists",
      content = @Content(schema = @Schema(implementation = ApiError.class)))
  public ResponseEntity<ProjectResponseDTO> createProject(
      @RequestBody @Valid ProjectRequestDTO request,
      @AuthenticationPrincipal AuthPrincipal authPrincipal) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(projectService.createProject(request, authPrincipal.userId()));
  }

  @GetMapping
  @CommonApiResponses
  @Operation(
      summary = "List projects",
      description = "Returns all projects belonging to the authenticated user.",
      security = @SecurityRequirement(name = "bearer-jwt"))
  @ApiResponse(responseCode = "200", description = "Project list returned")
  public ResponseEntity<List<ProjectResponseDTO>> getProjects(
      @AuthenticationPrincipal AuthPrincipal authPrincipal) {
    return ResponseEntity.ok(projectService.getProjects(authPrincipal.userId()));
  }

  @GetMapping("/{id}")
  @CommonApiResponses
  @Operation(
      summary = "Get a project",
      description = "Returns a single project by ID. Only accessible by the project owner.",
      security = @SecurityRequirement(name = "bearer-jwt"))
  @ApiResponse(responseCode = "200", description = "Project returned")
  @ApiResponse(
      responseCode = "404",
      description = "Project not found or does not belong to the user",
      content = @Content(schema = @Schema(implementation = ApiError.class)))
  public ResponseEntity<ProjectResponseDTO> getProject(
      @PathVariable UUID id, @AuthenticationPrincipal AuthPrincipal authPrincipal) {
    return ResponseEntity.ok(projectService.getProject(id, authPrincipal.userId()));
  }

  @DeleteMapping("/{id}")
  @CommonApiResponses
  @Operation(
      summary = "Delete a project",
      description = "Deletes a project by ID. Only the project owner can delete it.",
      security = @SecurityRequirement(name = "bearer-jwt"))
  @ApiResponse(responseCode = "204", description = "Project deleted successfully")
  @ApiResponse(
      responseCode = "404",
      description = "Project not found or does not belong to the user",
      content = @Content(schema = @Schema(implementation = ApiError.class)))
  public ResponseEntity<Void> deleteProject(
      @PathVariable UUID id, @AuthenticationPrincipal AuthPrincipal authPrincipal) {
    projectService.deleteProject(id, authPrincipal.userId());
    return ResponseEntity.noContent().build();
  }
}

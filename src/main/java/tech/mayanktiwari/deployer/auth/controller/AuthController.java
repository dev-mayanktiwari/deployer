package tech.mayanktiwari.deployer.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.mayanktiwari.deployer.auth.dto.AuthPrincipal;
import tech.mayanktiwari.deployer.auth.dto.AuthUserResponse;
import tech.mayanktiwari.deployer.auth.service.AuthService;
import tech.mayanktiwari.deployer.common.openapi.CommonApiResponses;
import tech.mayanktiwari.deployer.common.response.ApiError;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication and current-user endpoints")
public class AuthController {

  private final AuthService authService;

  @GetMapping("/me")
  @CommonApiResponses
  @Operation(
      summary = "Get current user",
      description = "Returns the profile of the currently authenticated user.",
      security = @SecurityRequirement(name = "bearer-jwt"))
  @ApiResponse(responseCode = "200", description = "Authenticated user returned")
  @ApiResponse(
      responseCode = "404",
      description = "User record not found",
      content = @Content(schema = @Schema(implementation = ApiError.class)))
  public ResponseEntity<AuthUserResponse> me(Authentication authentication) {
    AuthPrincipal principal = (AuthPrincipal) authentication.getPrincipal();
    return ResponseEntity.ok(authService.getMe(principal));
  }
}

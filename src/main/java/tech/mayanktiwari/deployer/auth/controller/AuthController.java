package tech.mayanktiwari.deployer.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.mayanktiwari.deployer.auth.dto.AuthPrincipal;
import tech.mayanktiwari.deployer.auth.dto.AuthUserResponse;
import tech.mayanktiwari.deployer.auth.service.AuthService;
import tech.mayanktiwari.deployer.common.response.ApiResponse;
import tech.mayanktiwari.deployer.common.response.ResponseBuilder;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthUserResponse>> me(Authentication authentication) {
        AuthPrincipal principal = (AuthPrincipal) authentication.getPrincipal();
        return ResponseBuilder.ok(authService.getMe(principal));
    }
}

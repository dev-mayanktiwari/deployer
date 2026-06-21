package tech.mayanktiwari.deployer.auth.dto;

import java.util.UUID;

public record AuthUserResponse(UUID id, String username, String email) {}

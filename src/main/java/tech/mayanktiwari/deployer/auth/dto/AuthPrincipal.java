package tech.mayanktiwari.deployer.auth.dto;

import java.util.UUID;

public record AuthPrincipal(UUID userId, String username) {}

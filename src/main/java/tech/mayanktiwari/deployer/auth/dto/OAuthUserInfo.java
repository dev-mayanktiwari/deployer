package tech.mayanktiwari.deployer.auth.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import tech.mayanktiwari.deployer.common.config.AuthProvider;

@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class OAuthUserInfo {

    String providerId;
    AuthProvider provider;
    String username;
    String email;
    String avatarUrl;
}

package tech.mayanktiwari.deployer.auth.dto;

import lombok.Builder;
import lombok.Data;
import tech.mayanktiwari.deployer.common.config.AuthProvider;

@Data
@Builder
public class OAuthUserInfo {

  String providerId;
  AuthProvider provider;
  String username;
  String email;
  String avatarUrl;

  public static OAuthUserInfo of(
      Object providerId, AuthProvider provider, String username, String email, String avatarUrl) {

    return OAuthUserInfo.builder()
        .providerId(String.valueOf(providerId))
        .provider(provider)
        .username(username)
        .email(email)
        .avatarUrl(avatarUrl)
        .build();
  }
}

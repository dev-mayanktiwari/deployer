package tech.mayanktiwari.deployer.auth.oauth;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import tech.mayanktiwari.deployer.auth.dto.OAuthUserInfo;
import tech.mayanktiwari.deployer.common.config.AuthProvider;

@Component
public class GithubOAuthUserInfoExtractor implements OAuthUserInfoExtractor {

  public static final String USERNAME = "login";
  public static final String EMAIL = "email";
  public static final String AVATAR_URL = "avatar_url";
  private static final String PROVIDER_ID = "id";

  @Override
  public String getProvider() {
    return "github";
  }

  @Override
  public OAuthUserInfo extractUserInfo(OAuth2User user) {
    return OAuthUserInfo.of(
        user.getAttribute(PROVIDER_ID),
        AuthProvider.GITHUB,
        user.getAttribute(USERNAME),
        user.getAttribute(EMAIL),
        user.getAttribute(AVATAR_URL));
  }
}

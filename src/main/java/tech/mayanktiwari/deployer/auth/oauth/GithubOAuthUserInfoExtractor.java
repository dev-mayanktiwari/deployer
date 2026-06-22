package tech.mayanktiwari.deployer.auth.oauth;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import tech.mayanktiwari.deployer.auth.dto.OAuthUserInfo;
import tech.mayanktiwari.deployer.common.config.AuthProvider;

@Component
@RequiredArgsConstructor
public class GithubOAuthUserInfoExtractor implements OAuthUserInfoExtractor {

  public static final String USERNAME = "login";
  public static final String EMAIL = "email";
  public static final String AVATAR_URL = "avatar_url";
  private static final String PROVIDER_ID = "id";

  private final GitHubApiClient gitHubApiClient;

  @Override
  public String getProvider() {
    return "github";
  }

  @Override
  public OAuthUserInfo extractUserInfo(OAuth2User user, String accessToken) {
    String email = user.getAttribute(EMAIL);

    if (Objects.isNull(email)) {
      email = gitHubApiClient.fetchPrimaryEmail(accessToken).orElse(null);
    }

    return OAuthUserInfo.of(
        user.getAttribute(PROVIDER_ID),
        AuthProvider.GITHUB,
        user.getAttribute(USERNAME),
        email,
        user.getAttribute(AVATAR_URL));
  }
}

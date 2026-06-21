package tech.mayanktiwari.deployer.auth.oauth;

import org.springframework.security.oauth2.core.user.OAuth2User;
import tech.mayanktiwari.deployer.auth.dto.OAuthUserInfo;

public interface OAuthUserInfoExtractor {

  String getProvider();

  OAuthUserInfo extractUserInfo(OAuth2User user, String accessToken);
}

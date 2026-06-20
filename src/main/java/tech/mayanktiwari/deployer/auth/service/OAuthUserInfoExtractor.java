package tech.mayanktiwari.deployer.auth.service;

import org.springframework.security.oauth2.core.user.OAuth2User;
import tech.mayanktiwari.deployer.auth.dto.OAuthUserInfo;
import tech.mayanktiwari.deployer.common.config.AuthProvider;

public interface OAuthUserInfoExtractor {

    AuthProvider getProvider();

    OAuthUserInfo extractUserInfo(OAuth2User user);

}

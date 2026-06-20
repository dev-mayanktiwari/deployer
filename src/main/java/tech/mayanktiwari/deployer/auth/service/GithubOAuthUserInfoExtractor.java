package tech.mayanktiwari.deployer.auth.service;

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
    public AuthProvider getProvider() {
        return AuthProvider.GITHUB;
    }

    @Override
    public OAuthUserInfo extractUserInfo(OAuth2User user) {
        return OAuthUserInfo
                .builder()
                .providerId(user.getAttribute(PROVIDER_ID))
                .username(user.getAttribute(USERNAME))
                .email(user.getAttribute(EMAIL))
                .avatarUrl(user.getAttribute(AVATAR_URL))
                .provider(AuthProvider.GITHUB)
                .build();
    }
}

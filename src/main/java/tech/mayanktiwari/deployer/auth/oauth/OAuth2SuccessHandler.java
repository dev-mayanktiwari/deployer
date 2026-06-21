package tech.mayanktiwari.deployer.auth.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tech.mayanktiwari.deployer.auth.dto.OAuthUserInfo;
import tech.mayanktiwari.deployer.auth.service.AuthService;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private static final String REDIRECT_URL = "/auth/callback?token=";
  private final AuthService authService;
  private final OAuth2AuthorizedClientService authorizedClientService;

  /**
   * key = provider name <br>
   * value = extractor bean
   *
   * <p>github -> GitHubOAuthExtractor <br>
   * google -> GoogleOAuthExtractor
   */
  private final Map<String, OAuthUserInfoExtractor> extractors;

  @Value("${app.frontend-url}")
  private String frontendUrl;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
    String provider = authToken.getAuthorizedClientRegistrationId();

    OAuth2User authenticatedUser = authToken.getPrincipal();

    OAuth2AuthorizedClient authorizedClient =
        authorizedClientService.loadAuthorizedClient(provider, authToken.getName());

    if (Objects.isNull(authorizedClient)) {
      throw new IllegalStateException("Authorized client not found for provider: " + provider);
    }

    String accessToken = authorizedClient.getAccessToken().getTokenValue();
    OAuthUserInfoExtractor extractor = extractors.get(provider);

    if (Objects.isNull(extractor)) {
      throw new IllegalStateException("Extractor not found for provider: " + provider);
    }

    OAuthUserInfo userInfo = extractor.extractUserInfo(authenticatedUser);

    String jwtToken = authService.handleOAuth2Login(userInfo, accessToken);

    String redirectUrl = frontendUrl + REDIRECT_URL + jwtToken;

    getRedirectStrategy().sendRedirect(request, response, redirectUrl);
  }
}

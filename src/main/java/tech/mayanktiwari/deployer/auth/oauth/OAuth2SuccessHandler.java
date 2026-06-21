package tech.mayanktiwari.deployer.auth.oauth;

import static tech.mayanktiwari.deployer.common.config.Constants.AUTH_COOKIE;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
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

  private final AuthService authService;
  private final OAuth2AuthorizedClientService authorizedClientService;

  private final Map<String, OAuthUserInfoExtractor> extractors;

  @Value("${app.frontend-url}")
  private String frontendUrl;

  @Value("${app.jwt.expiration-ms}")
  private long jwtExpirationMs;

  @Value("${app.cookie.secure}")
  private boolean cookieSecure;

  @Value("${app.cookie.same-site}")
  private String cookieSameSite;

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

    OAuthUserInfo userInfo = extractor.extractUserInfo(authenticatedUser, accessToken);
    String jwtToken = authService.handleOAuth2Login(userInfo, accessToken);

    Cookie cookie = new Cookie(AUTH_COOKIE, jwtToken);
    cookie.setHttpOnly(true);
    cookie.setSecure(cookieSecure);
    cookie.setPath("/");
    cookie.setMaxAge((int) (jwtExpirationMs / 1000));
    // SameSite must be set via the header — the Cookie API doesn't support it directly
    response.addCookie(cookie);
    response.setHeader(
        "Set-Cookie",
        String.format(
            "%s=%s; Path=/; Max-Age=%d; HttpOnly; SameSite=%s%s",
            AUTH_COOKIE,
            jwtToken,
            (int) (jwtExpirationMs / 1000),
            cookieSameSite,
            cookieSecure ? "; Secure" : ""));

    getRedirectStrategy().sendRedirect(request, response, frontendUrl);
  }
}

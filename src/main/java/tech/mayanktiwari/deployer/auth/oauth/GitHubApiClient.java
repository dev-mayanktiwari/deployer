package tech.mayanktiwari.deployer.auth.oauth;

import static tech.mayanktiwari.deployer.common.config.Constants.BEARER;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
public class GitHubApiClient {

  private static final String USER_EMAILS_PATH = "/user/emails";

  private final RestClient restClient;

  public GitHubApiClient(@Qualifier("gitHubRestClient") RestClient restClient) {
    this.restClient = restClient;
  }

  public Optional<String> fetchPrimaryEmail(String accessToken) {
    try {
      GitHubEmail[] emails =
          restClient
              .get()
              .uri(USER_EMAILS_PATH)
              .header(HttpHeaders.AUTHORIZATION, BEARER + accessToken)
              .retrieve()
              .body(GitHubEmail[].class);

      if (Objects.isNull(emails)) return Optional.empty();

      return Arrays.stream(emails)
          .filter(e -> e.primary() && e.verified())
          .map(GitHubEmail::email)
          .findFirst();
    } catch (Exception e) {
      log.warn("Failed to fetch GitHub primary email: {}", e.getMessage());
      return Optional.empty();
    }
  }

  private record GitHubEmail(String email, boolean primary, boolean verified) {}
}

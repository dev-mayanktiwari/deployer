package tech.mayanktiwari.deployer.auth.oauth;

import java.util.Arrays;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
public class GitHubApiClient {

  private record GitHubEmail(String email, boolean primary, boolean verified) {}

  private final RestClient restClient;

  public GitHubApiClient(@Value("${app.github.api-base-url}") String baseUrl) {
    this.restClient = RestClient.builder().baseUrl(baseUrl).build();
  }

  public Optional<String> fetchPrimaryEmail(String accessToken) {
    try {
      GitHubEmail[] emails =
          restClient
              .get()
              .uri("/user/emails")
              .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
              .retrieve()
              .body(GitHubEmail[].class);

      if (emails == null) return Optional.empty();

      return Arrays.stream(emails)
          .filter(e -> e.primary() && e.verified())
          .map(GitHubEmail::email)
          .findFirst();
    } catch (Exception e) {
      log.warn("Failed to fetch GitHub primary email: {}", e.getMessage());
      return Optional.empty();
    }
  }
}

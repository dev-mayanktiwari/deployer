package tech.mayanktiwari.deployer.auth.config;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import tech.mayanktiwari.deployer.auth.oauth.OAuthUserInfoExtractor;

@Configuration
public class OAuthConfig {

  @Bean
  public Map<String, OAuthUserInfoExtractor> extractors(
      List<OAuthUserInfoExtractor> extractorList) {
    return extractorList.stream()
        .collect(Collectors.toMap(OAuthUserInfoExtractor::getProvider, Function.identity()));
  }

  @Bean
  public RestClient gitHubRestClient(
      RestClient.Builder builder, @Value("${app.github.api-base-url}") String baseUrl) {
    return builder.baseUrl(baseUrl).build();
  }
}

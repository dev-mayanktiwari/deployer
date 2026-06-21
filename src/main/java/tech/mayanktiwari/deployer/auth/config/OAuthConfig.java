package tech.mayanktiwari.deployer.auth.config;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.mayanktiwari.deployer.auth.oauth.OAuthUserInfoExtractor;

@Configuration
public class OAuthConfig {

  @Bean
  public Map<String, OAuthUserInfoExtractor> extractors(
      List<OAuthUserInfoExtractor> extractorList) {
    return extractorList.stream()
        .collect(Collectors.toMap(OAuthUserInfoExtractor::getProvider, Function.identity()));
  }
}

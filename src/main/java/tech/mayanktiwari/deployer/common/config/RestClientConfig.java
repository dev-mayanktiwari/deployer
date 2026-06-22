package tech.mayanktiwari.deployer.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.mayanktiwari.deployer.common.logging.HttpRequestLoggingInterceptor;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

  private final HttpRequestLoggingInterceptor loggingInterceptor;

  @Bean
  public RestClientCustomizer restClientCustomizer() {
    return builder -> builder.requestInterceptor(loggingInterceptor);
  }
}

package tech.mayanktiwari.deployer.common.config;

import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestClientConfig {

  /**
   * Applies common configuration to every RestClient.Builder created in the application. Add
   * timeouts, logging interceptors, metrics, etc. here as the project grows.
   */
  @Bean
  public RestClientCustomizer restClientCustomizer() {
    return builder -> {
      // e.g. builder.requestInterceptor(loggingInterceptor)
    };
  }
}

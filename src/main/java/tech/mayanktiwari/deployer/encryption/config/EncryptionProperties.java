package tech.mayanktiwari.deployer.encryption.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@ConfigurationProperties(prefix = "encryption")
@Validated
public final class EncryptionProperties {

  @NotBlank private final String secret;

  EncryptionProperties(String secret) {
    this.secret = secret;
  }
}

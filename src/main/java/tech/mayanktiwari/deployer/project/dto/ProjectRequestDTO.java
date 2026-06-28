package tech.mayanktiwari.deployer.project.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.URL;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProjectRequestDTO {

  @NotBlank String name;

  @NotBlank @URL String repositoryUrl;

  @NotBlank String branch;

  @NotBlank String buildCommand;

  @NotBlank String outputPath;
}

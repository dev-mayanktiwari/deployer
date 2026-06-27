package tech.mayanktiwari.deployer.project.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ProjectResponseDTO {

  UUID id;
  String name;
  String repositoryUrl;
  String branch;
  String buildCommand;
  String outputPath;
  LocalDateTime createdAt;
}

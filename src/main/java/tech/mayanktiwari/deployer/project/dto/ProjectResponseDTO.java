package tech.mayanktiwari.deployer.project.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateProjectResponseDTO {

    UUID id;
    String name;
    String repositoryUrl;
    String branch;
    String buildCommand;
    String outputPath;
    Instant createdAt;
    
}
